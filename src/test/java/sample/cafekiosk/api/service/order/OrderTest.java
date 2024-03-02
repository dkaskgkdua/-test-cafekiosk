package sample.cafekiosk.api.service.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.domain.order.Order;
import sample.cafekiosk.domain.order.OrderStatus;
import sample.cafekiosk.domain.product.Product;
import sample.cafekiosk.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.domain.product.ProductSellingStatus.SELLING;

@SpringBootTest
@ActiveProfiles("local")
public class OrderTest {

    @DisplayName("상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        List<Product> products = List.of(
                createProduct("001", 4000),
                createProduct("002", 4500),
                createProduct("003", 7000)
        );


        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(15500);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
    @Test
    void init() {
        // given
        List<Product> products = List.of(
                createProduct("001", 4000),
                createProduct("002", 4500)
        );
        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시 등록 시간을 기록한다.")
    @Test
    void registeredDateTime() {
        // given
        List<Product> products = List.of(
                createProduct("001", 4000),
                createProduct("002", 4500)
        );
        // when
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Order order = Order.create(products, registeredDateTime);

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(ProductType.HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(price)
                .name("상품")
                .build();
    }
}
