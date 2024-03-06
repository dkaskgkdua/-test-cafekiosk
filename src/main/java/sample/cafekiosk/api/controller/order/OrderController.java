package sample.cafekiosk.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.api.ApiResponse;
import sample.cafekiosk.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.api.controller.order.response.OrderResponse;
import sample.cafekiosk.api.service.order.OrderService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public ApiResponse<OrderResponse> createOrder(@Validated @RequestBody OrderCreateRequest request) {
        return ApiResponse.ok(orderService.createOrder(request.toServiceRequest(), LocalDateTime.now()));

    }
}
