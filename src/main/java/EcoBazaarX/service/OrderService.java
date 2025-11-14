package EcoBazaarX.service;

import EcoBazaarX.entity.*;
import EcoBazaarX.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private AuthService authService;
    
    @Transactional
    public Order checkout() {
        User user = authService.getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout with empty cart");
        }
        
        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(cart.getTotalPrice());
        order.setTotalCarbon(cart.getTotalCarbon());
        order.setStatus(Order.OrderStatus.PENDING);
        
        // Copy cart items to order items
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(cartItem.getProduct().getProductId());
            orderItem.setProductName(cartItem.getProduct().getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getPrice());
            orderItem.setCarbonAtPurchase(cartItem.getCarbonFootprint());
            order.getItems().add(orderItem);
            
            // Update product stock
            Product product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        order = orderRepository.save(order);
        
        // Update user profile with carbon points
        updateUserProfile(user, cart);
        
        // Clear cart
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cart.setTotalCarbon(0.0);
        cartRepository.save(cart);
        
        return order;
    }
    
    public List<Order> getMyOrders() {
        User user = authService.getCurrentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    private void updateUserProfile(User user, Cart cart) {
        UserProfile profile = userProfileRepository.findByUser(user).orElseGet(() -> {
            UserProfile newProfile = new UserProfile();
            newProfile.setUser(user);
            return newProfile;
        });
        
        // Calculate total points from cart items
        int totalPoints = cart.getItems().stream()
                .mapToInt(item -> {
                    Product product = item.getProduct();
                    return product.getCarbonPoints() * item.getQuantity();
                })
                .sum();
        
        profile.setCarbonPoints(profile.getCarbonPoints() + totalPoints);
        profile.setCarbonSaved(profile.getCarbonSaved() + cart.getTotalCarbon());
        
        // Update badge
        if (profile.getCarbonPoints() >= 500) {
            profile.setBadge("Gold");
        } else if (profile.getCarbonPoints() >= 200) {
            profile.setBadge("Silver");
        } else {
            profile.setBadge("Bronze");
        }
        
        userProfileRepository.save(profile);
    }
}
