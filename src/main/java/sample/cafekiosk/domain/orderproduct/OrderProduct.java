package sample.cafekiosk.domain.orderproduct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.domain.order.Order;
import sample.cafekiosk.domain.product.BaseEntity;
import sample.cafekiosk.domain.product.Product;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_PRODUCT_SQ")
    @SequenceGenerator(sequenceName = "ORDER_PRODUCT_SQ", name = "ORDER_PRODUCT_SQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }
}
