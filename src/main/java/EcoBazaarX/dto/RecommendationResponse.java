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
public class RecommendationResponse {
    private Long productId;
    private String name;
    private String description;
    private String category;
    private Double price;
    private Double carbonFootprint;
    private String imageUrl;
    private Double score;
    private String reason;
}
