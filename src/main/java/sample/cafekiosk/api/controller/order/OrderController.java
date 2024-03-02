package sample.cafekiosk.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.api.controller.order.response.OrderResponse;
import sample.cafekiosk.api.service.order.OrderService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request, LocalDateTime.now());

    }
}
