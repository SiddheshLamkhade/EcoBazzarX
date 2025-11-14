package EcoBazaarX.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    private String category;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "carbon_footprint")
    private Double carbonFootprint;
    
    @Column(name = "carbon_explanation", length = 2000)
    private String carbonExplanation;
    
    @Column(name = "carbon_points")
    private Integer carbonPoints;
    
    // Carbon Details Fields (embedded in products table)
    private String volume;
    private String weight;
    
    @Column(name = "material_composition")
    private String materialComposition;
    
    @Column(name = "manufacturing_location")
    private String manufacturingLocation;
    
    @Column(name = "electricity_type")
    private String electricityType;
    
    @Column(name = "manufacturing_energy_used")
    private String manufacturingEnergyUsed;
    
    @Column(name = "packaging_details", length = 1000)
    private String packagingDetails;
    
    @Column(name = "shipping_mode")
    private String shippingMode;
    
    @Column(name = "sea_freight_distance")
    private Double seaFreightDistance;
    
    @Column(name = "truck_distance")
    private Double truckDistance;
    
    private String lifespan;
    
    @Column(name = "power_usage")
    private String powerUsage;
    
    @Column(name = "recyclability_rate")
    private Double recyclabilityRate;
    
    @Column(name = "biodegradability_rate")
    private Double biodegradabilityRate;
    
    @Column(name = "posted_by")
    private Long postedBy;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
