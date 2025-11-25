package EcoBazaarX.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerOrderItemResponse {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double priceAtPurchase;
    private Double carbonAtPurchase;
}
