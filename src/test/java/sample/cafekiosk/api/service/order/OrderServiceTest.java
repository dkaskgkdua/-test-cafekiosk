package sample.cafekiosk.api.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.api.controller.order.response.OrderResponse;
import sample.cafekiosk.domain.product.Product;
import sample.cafekiosk.domain.product.ProductRepository;
import sample.cafekiosk.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.domain.product.ProductSellingStatus.SELLING;

@SpringBootTest
@ActiveProfiles("local")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        LocalDateTime regisDateTime = LocalDateTime.now();

        Product product1 =createProduct(ProductType.HANDMADE, "001", 4000);
        Product product2 =createProduct(ProductType.HANDMADE, "002", 4500);
        Product product3 =createProduct(ProductType.HANDMADE, "003", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        // when

        OrderResponse response = orderService.createOrder(request, regisDateTime);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(regisDateTime, 8500);
        assertThat(response.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("002", 4500)
                );
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(price)
                .name("상품")
                .build();
    }

}