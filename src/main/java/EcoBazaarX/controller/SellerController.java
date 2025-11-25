package EcoBazaarX.controller;

import EcoBazaarX.dto.AddProductRequest;
import EcoBazaarX.dto.SellerDashboardStats;
import EcoBazaarX.dto.SellerOrderResponse;
import EcoBazaarX.dto.UpdateProductRequest;
import EcoBazaarX.entity.Product;
import EcoBazaarX.service.OrderService;
import EcoBazaarX.service.ProductService;
import EcoBazaarX.service.SellerDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('SELLER')")
public class SellerController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private SellerDashboardService sellerDashboardService;

    @Autowired
    private OrderService orderService;
    
    @PostMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestBody AddProductRequest request) {
        try {
            String message = productService.addProduct(request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/my-products")
    public ResponseEntity<List<Product>> getMyProducts() {
        return ResponseEntity.ok(productService.getSellerProducts());
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<SellerDashboardStats> getDashboardStats() {
        return ResponseEntity.ok(sellerDashboardService.getDashboardStats());
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<SellerOrderResponse>> getSellerOrders() {
        return ResponseEntity.ok(orderService.getSellerOrders());
    }
    
    @PutMapping("/update-product/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @RequestBody UpdateProductRequest request) {
        try {
            String message = productService.updateProduct(productId, request);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        try {
            String message = productService.deleteProduct(productId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
