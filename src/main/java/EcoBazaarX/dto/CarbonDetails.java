package EcoBazaarX.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarbonDetails {
    private String productName;
    private String volume;
    private String weight;
    private String materialComposition;
    private String manufacturingLocation;
    private String electricityType;
    private String manufacturingEnergyUsed;
    private String packagingDetails;
    private String shippingMode;
    private Double seaFreightDistance;
    private Double truckDistance;
    private String lifespan;
    private String powerUsage;
    private Double recyclabilityRate;
    private Double biodegradabilityRate;
}
