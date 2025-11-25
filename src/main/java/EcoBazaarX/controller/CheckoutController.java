package EcoBazaarX.controller;

import EcoBazaarX.entity.Order;
import EcoBazaarX.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/checkout", "/checkout"})
@CrossOrigin(origins = "*")
public class CheckoutController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping("/add-checkout")
    public ResponseEntity<Order> checkout() {
        try {
            Order order = orderService.checkout();
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }
    
    @PutMapping("/status/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status) {
        try {
            Order order = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
