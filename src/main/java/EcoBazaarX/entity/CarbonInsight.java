package EcoBazaarX.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "carbon_insight")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarbonInsight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "cart"})
    private User user;
    
    @Column(name = "product_name")
    private String productName;
    
    @Column(name = "estimated_carbon")
    private Double estimatedCarbon;
    
    @CreationTimestamp
    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;
}
