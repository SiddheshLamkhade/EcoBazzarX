package EcoBazaarX.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    private String name;
    private String description;
    private String category;
    private Double price;
    private Integer stockQuantity;
    private String imageUrl;
    private CarbonDetails carbonDetails;
}
