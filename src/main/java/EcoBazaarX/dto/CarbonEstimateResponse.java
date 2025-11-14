package EcoBazaarX.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarbonEstimateResponse {
    private Double estimatedCarbonFootprint;
    private String explanation;
}
