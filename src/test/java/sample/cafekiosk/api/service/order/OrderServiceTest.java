package sample.cafekiosk.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.api.controller.order.response.OrderResponse;
import sample.cafekiosk.api.service.order.request.OrderCreateServiceRequest;
import sample.cafekiosk.domain.order.OrderRepository;
import sample.cafekiosk.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.domain.product.Product;
import sample.cafekiosk.domain.product.ProductRepository;
import sample.cafekiosk.domain.product.ProductType;
import sample.cafekiosk.domain.stock.Stock;
import sample.cafekiosk.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.domain.product.ProductSellingStatus.SELLING;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

//    @AfterEach
//    void tearDown() {
//        orderProductRepository.deleteAllInBatch();
//        productRepository.deleteAllInBatch();
//        orderRepository.deleteAllInBatch();
//    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        // given
        LocalDateTime regisDateTime = LocalDateTime.now();

        Product product1 =createProduct(ProductType.BOTTLE, "001", 4000);
        Product product2 =createProduct(ProductType.BAKERY, "002", 4500);
        Product product3 =createProduct(ProductType.HANDMADE, "003", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001","001", "002", "003"))
                .build();

        // when

        OrderResponse response = orderService.createOrder(request, regisDateTime);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(regisDateTime, 19500);
        assertThat(response.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000),
                        tuple("002", 4500),
                        tuple("003", 7000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );
    }

    @DisplayName("재고가 없는 상품으로 주문을 생성하려는 경우 예외가 발생")
    @Test
    void createOrderWithNoStock() {
        // given
        LocalDateTime regisDateTime = LocalDateTime.now();

        Product product1 =createProduct(ProductType.BOTTLE, "001", 4000);
        Product product2 =createProduct(ProductType.BAKERY, "002", 4500);
        Product product3 =createProduct(ProductType.HANDMADE, "003", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stock1.deductQuantity(1);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001","001", "002", "003"))
                .build();

        // when  // then
        assertThatThrownBy(() -> orderService.createOrder(request, regisDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");

    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // given
        LocalDateTime regisDateTime = LocalDateTime.now();

        Product product1 =createProduct(ProductType.HANDMADE, "001", 4000);
        Product product2 =createProduct(ProductType.HANDMADE, "002", 4500);
        Product product3 =createProduct(ProductType.HANDMADE, "003", 7000);

        productRepository.saveAll(List.of(product1, product2, product3));
        OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        // when

        OrderResponse response = orderService.createOrder(request, regisDateTime);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
                .extracting("registeredDateTime", "totalPrice")
                .contains(regisDateTime, 8000);
        assertThat(response.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000)
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