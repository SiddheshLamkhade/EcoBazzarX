package EcoBazaarX.service;

import EcoBazaarX.dto.AddProductRequest;
import EcoBazaarX.dto.CarbonEstimateResponse;
import EcoBazaarX.dto.UpdateProductRequest;
import EcoBazaarX.entity.Product;
import EcoBazaarX.entity.User;
import EcoBazaarX.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CarbonService carbonService;
    
    @Autowired
    private AuthService authService;
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public List<Product> filterProducts(String name, String category, Double minPrice, 
                                        Double maxPrice, Double maxCarbon) {
        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (maxCarbon != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("carbonFootprint"), maxCarbon));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return productRepository.findAll(spec);
    }
    
    @Transactional
    public String addProduct(AddProductRequest request) {
        User seller = authService.getCurrentUser();
        
        // Calculate carbon footprint using AI
        CarbonEstimateResponse carbonEstimate = carbonService.estimateCarbonFootprint(request.getCarbonDetails());
        
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCarbonFootprint(carbonEstimate.getEstimatedCarbonFootprint());
        product.setCarbonExplanation(carbonEstimate.getExplanation());
        product.setCarbonPoints(calculateCarbonPoints(carbonEstimate.getEstimatedCarbonFootprint()));
        product.setPostedBy(seller.getId());
        
        // Set carbon details fields from request
        if (request.getCarbonDetails() != null) {
            product.setVolume(request.getCarbonDetails().getVolume());
            product.setWeight(request.getCarbonDetails().getWeight());
            product.setMaterialComposition(request.getCarbonDetails().getMaterialComposition());
            product.setManufacturingLocation(request.getCarbonDetails().getManufacturingLocation());
            product.setElectricityType(request.getCarbonDetails().getElectricityType());
            product.setManufacturingEnergyUsed(request.getCarbonDetails().getManufacturingEnergyUsed());
            product.setPackagingDetails(request.getCarbonDetails().getPackagingDetails());
            product.setShippingMode(request.getCarbonDetails().getShippingMode());
            product.setSeaFreightDistance(request.getCarbonDetails().getSeaFreightDistance());
            product.setTruckDistance(request.getCarbonDetails().getTruckDistance());
            product.setLifespan(request.getCarbonDetails().getLifespan());
            product.setPowerUsage(request.getCarbonDetails().getPowerUsage());
            product.setRecyclabilityRate(request.getCarbonDetails().getRecyclabilityRate());
            product.setBiodegradabilityRate(request.getCarbonDetails().getBiodegradabilityRate());
        }
        
        productRepository.save(product);
        
        return String.format("✅ Product added successfully by %s | Carbon footprint: %.1f kg CO₂e | Points: %d",
                seller.getUsername(), carbonEstimate.getEstimatedCarbonFootprint(), product.getCarbonPoints());
    }
    
    public List<Product> getSellerProducts() {
        User seller = authService.getCurrentUser();
        return productRepository.findByPostedBy(seller.getId());
    }
    
    @Transactional
    public String updateProduct(Long productId, UpdateProductRequest request) {
        User seller = authService.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (!product.getPostedBy().equals(seller.getId())) {
            throw new RuntimeException("You can only update your own products");
        }
        
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getStockQuantity() != null) product.setStockQuantity(request.getStockQuantity());
        
        productRepository.save(product);
        return "Product updated successfully!";
    }
    
    @Transactional
    public String deleteProduct(Long productId) {
        User seller = authService.getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (!product.getPostedBy().equals(seller.getId())) {
            throw new RuntimeException("You can only delete your own products");
        }
        
        productRepository.delete(product);
        return "Product deleted successfully!";
    }
    
    private Integer calculateCarbonPoints(Double carbonFootprint) {
        if (carbonFootprint <= 1.0) return 50;
        if (carbonFootprint <= 3.0) return 30;
        if (carbonFootprint <= 5.0) return 20;
        if (carbonFootprint <= 10.0) return 10;
        return 5;
    }
}
