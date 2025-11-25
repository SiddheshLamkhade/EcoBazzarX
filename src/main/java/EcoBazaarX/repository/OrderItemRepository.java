package EcoBazaarX.repository;

import EcoBazaarX.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
            SELECT COALESCE(SUM(oi.priceAtPurchase * oi.quantity), 0)
            FROM OrderItem oi
            JOIN oi.order o
            JOIN Product p ON p.productId = oi.productId
            WHERE p.postedBy = :sellerId
            """)
    Double sumRevenueForSeller(@Param("sellerId") Long sellerId);

    @Query("""
            SELECT COALESCE(SUM(oi.priceAtPurchase * oi.quantity), 0)
            FROM OrderItem oi
            JOIN oi.order o
            JOIN Product p ON p.productId = oi.productId
            WHERE p.postedBy = :sellerId
              AND o.createdAt BETWEEN :start AND :end
            """)
    Double sumRevenueForSellerBetween(
            @Param("sellerId") Long sellerId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
            SELECT COUNT(DISTINCT oi.order.id)
            FROM OrderItem oi
            JOIN Product p ON p.productId = oi.productId
            WHERE p.postedBy = :sellerId
            """)
    Long countDistinctOrdersForSeller(@Param("sellerId") Long sellerId);

    @Query("""
            SELECT oi
            FROM OrderItem oi
            JOIN FETCH oi.order o
            JOIN Product p ON p.productId = oi.productId
            WHERE p.postedBy = :sellerId
            ORDER BY o.createdAt DESC, oi.id DESC
            """)
    List<OrderItem> findItemsForSeller(@Param("sellerId") Long sellerId);
}
