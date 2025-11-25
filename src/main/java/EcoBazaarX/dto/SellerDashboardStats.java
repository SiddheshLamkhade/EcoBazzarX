package EcoBazaarX.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerDashboardStats {
    private long totalProducts;
    private long totalOrders;
    private double revenue;
    private double growth;
}
