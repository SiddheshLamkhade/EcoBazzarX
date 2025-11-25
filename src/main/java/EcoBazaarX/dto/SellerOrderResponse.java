package EcoBazaarX.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerOrderResponse {
    private Long orderId;
    private String status;
    private LocalDateTime orderDate;
    private String customerName;
    private String customerUsername;
    private Double totalAmount;
    @Builder.Default
    private List<SellerOrderItemResponse> items = new ArrayList<>();

    public void addItem(SellerOrderItemResponse item) {
        this.items.add(item);
    }
}
