package EcoBazaarX.controller;

import EcoBazaarX.dto.UpdateUserRequest;
import EcoBazaarX.dto.UserResponse;
import EcoBazaarX.entity.Order;
import EcoBazaarX.entity.User;
import EcoBazaarX.service.AuthService;
import EcoBazaarX.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/all-users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }
    
    @GetMapping("/all-sellers")
    public ResponseEntity<List<UserResponse>> getAllSellers() {
        return ResponseEntity.ok(authService.getUsersByRole(User.Role.SELLER));
    }
    
    @GetMapping("/all-customers")
    public ResponseEntity<List<UserResponse>> getAllCustomers() {
        return ResponseEntity.ok(authService.getUsersByRole(User.Role.USER));
    }
    
    @GetMapping("/all-admins")
    public ResponseEntity<List<UserResponse>> getAllAdmins() {
        return ResponseEntity.ok(authService.getUsersByRole(User.Role.ADMIN));
    }
    
    @PutMapping("/change-role/{username}")
    public ResponseEntity<String> changeUserRole(
            @PathVariable String username,
            @RequestParam User.Role role) {
        try {
            String message = authService.changeUserRole(username, role);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            String message = authService.deleteUser(id);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        try {
            UserResponse response = authService.updateUser(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/all-orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
