package EcoBazaarX.controller;

import EcoBazaarX.entity.Cart;
import EcoBazaarX.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @PostMapping("/add/{productId}/{quantity}")
    public ResponseEntity<Cart> addToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        try {
            Cart cart = cartService.addToCart(productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/view")
    public ResponseEntity<Cart> viewCart() {
        try {
            Cart cart = cartService.viewCart();
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long productId) {
        try {
            Cart cart = cartService.removeFromCart(productId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        try {
            String message = cartService.clearCart();
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/update/{productId}/{quantity}")
    public ResponseEntity<Cart> updateCartItemQuantity(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        try {
            Cart cart = cartService.updateCartItemQuantity(productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
