package EcoBazaarX.service;

import EcoBazaarX.dto.SellerDashboardStats;
import EcoBazaarX.entity.User;
import EcoBazaarX.repository.OrderItemRepository;
import EcoBazaarX.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SellerDashboardService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AuthService authService;

    public SellerDashboardStats getDashboardStats() {
        User seller = authService.getCurrentUser();
        Long sellerId = seller.getId();

        long totalProducts = productRepository.countByPostedBy(sellerId);
        long totalOrders = safeLong(orderItemRepository.countDistinctOrdersForSeller(sellerId));
        double revenue = safeDouble(orderItemRepository.sumRevenueForSeller(sellerId));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last30Start = now.minusDays(30);
        LocalDateTime prev30Start = now.minusDays(60);

        double currentWindowRevenue = safeDouble(
                orderItemRepository.sumRevenueForSellerBetween(sellerId, last30Start, now));
        double previousWindowRevenue = safeDouble(
                orderItemRepository.sumRevenueForSellerBetween(sellerId, prev30Start, last30Start));

        double growth;
        if (previousWindowRevenue > 0) {
            growth = ((currentWindowRevenue - previousWindowRevenue) / previousWindowRevenue) * 100.0;
        } else if (currentWindowRevenue > 0) {
            growth = 100.0;
        } else {
            growth = 0.0;
        }

        SellerDashboardStats stats = new SellerDashboardStats();
        stats.setTotalProducts(totalProducts);
        stats.setTotalOrders(totalOrders);
        stats.setRevenue(revenue);
        stats.setGrowth(growth);
        return stats;
    }

    private long safeLong(Long value) {
        return value != null ? value : 0L;
    }

    private double safeDouble(Double value) {
        return value != null ? value : 0.0;
    }
}
