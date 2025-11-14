package EcoBazaarX.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnoreProperties({"password", "cart"})
    private User user;
    
    @Column(name = "carbon_points")
    private Integer carbonPoints = 0;
    
    @Column(name = "carbon_saved")
    private Double carbonSaved = 0.0;
    
    private String badge = "Bronze";
}
