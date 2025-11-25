package EcoBazaarX.service;

import EcoBazaarX.dto.SellerOrderItemResponse;
import EcoBazaarX.dto.SellerOrderResponse;
import EcoBazaarX.entity.*;
import EcoBazaarX.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private OrderItemRepository orderItemRepository;
    
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

    public List<SellerOrderResponse> getSellerOrders() {
        User seller = authService.getCurrentUser();
        List<OrderItem> sellerItems = orderItemRepository.findItemsForSeller(seller.getId());
        Map<Long, SellerOrderResponse> grouped = new LinkedHashMap<>();

        for (OrderItem item : sellerItems) {
            Order order = item.getOrder();
            SellerOrderResponse response = grouped.computeIfAbsent(order.getId(), id -> SellerOrderResponse.builder()
                    .orderId(order.getId())
                    .status(order.getStatus().name())
                    .orderDate(order.getCreatedAt())
                    .customerName(buildCustomerName(order.getUser()))
                    .customerUsername(order.getUser().getUsername())
                    .totalAmount(0.0)
                    .build());

            double lineTotal = (item.getPriceAtPurchase() != null ? item.getPriceAtPurchase() : 0.0)
                    * (item.getQuantity() != null ? item.getQuantity() : 0);

            SellerOrderItemResponse itemResponse = SellerOrderItemResponse.builder()
                    .productId(item.getProductId())
                    .productName(item.getProductName())
                    .quantity(item.getQuantity())
                    .priceAtPurchase(item.getPriceAtPurchase())
                    .carbonAtPurchase(item.getCarbonAtPurchase())
                    .build();

            response.addItem(itemResponse);
            response.setTotalAmount(response.getTotalAmount() + lineTotal);
        }

        return List.copyOf(grouped.values());
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

    private String buildCustomerName(User customer) {
        if (customer == null) {
            return "";
        }
        String first = customer.getFirstName() != null ? customer.getFirstName() : "";
        String last = customer.getLastName() != null ? customer.getLastName() : "";
        String full = (first + " " + last).trim();
        return full.isEmpty() ? customer.getUsername() : full;
    }
}
