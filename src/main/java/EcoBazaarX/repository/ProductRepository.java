package EcoBazaarX.repository;

import EcoBazaarX.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByPostedBy(Long postedBy);
    long countByPostedBy(Long postedBy);

    List<Product> findTop8ByCategoryIgnoreCaseAndProductIdNotOrderByCarbonFootprintAsc(String category, Long productId);

    List<Product> findTop8ByCarbonFootprintIsNotNullOrderByCarbonFootprintAsc();

    List<Product> findTop8ByCarbonFootprintLessThanAndProductIdNotOrderByCarbonFootprintAsc(Double carbonFootprint, Long productId);
}
