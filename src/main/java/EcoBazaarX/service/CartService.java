package EcoBazaarX.service;

import EcoBazaarX.entity.*;
import EcoBazaarX.repository.CartRepository;
import EcoBazaarX.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private AuthService authService;
    
    @Transactional
    public Cart addToCart(Long productId, Integer quantity) {
        User user = authService.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
        
        // Check if product already in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            newItem.setCarbonFootprint(product.getCarbonFootprint());
            cart.getItems().add(newItem);
        }
        
        updateCartTotals(cart);
        return cartRepository.save(cart);
    }
    
    public Cart viewCart() {
        User user = authService.getCurrentUser();
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));
    }
    
    @Transactional
    public Cart removeFromCart(Long productId) {
        User user = authService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        cart.getItems().removeIf(item -> item.getProduct().getProductId().equals(productId));
        updateCartTotals(cart);
        return cartRepository.save(cart);
    }
    
    @Transactional
    public String clearCart() {
        User user = authService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cart.setTotalCarbon(0.0);
        cartRepository.save(cart);
        
        return "Cart cleared successfully for user: " + user.getUsername();
    }
    
    @Transactional
    public Cart updateCartItemQuantity(Long productId, Integer quantity) {
        User user = authService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not in cart"));
        
        item.setQuantity(quantity);
        updateCartTotals(cart);
        return cartRepository.save(cart);
    }
    
    private void updateCartTotals(Cart cart) {
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        double totalCarbon = cart.getItems().stream()
                .mapToDouble(item -> item.getCarbonFootprint() * item.getQuantity())
                .sum();
        
        cart.setTotalPrice(totalPrice);
        cart.setTotalCarbon(totalCarbon);
    }
}
