package EcoBazaarX.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnoreProperties({"items", "user"})
    private Cart cart;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"postedBy"})
    private Product product;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(name = "carbon_footprint")
    private Double carbonFootprint;
}
