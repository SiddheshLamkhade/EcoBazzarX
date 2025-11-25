package EcoBazaarX.controller;

import EcoBazaarX.dto.RecommendationResponse;
import EcoBazaarX.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/personalized/{userId}")
    public ResponseEntity<List<RecommendationResponse>> getPersonalized(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(recommendationService.getPersonalizedRecommendations(userId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/similar/{productId}")
    public ResponseEntity<List<RecommendationResponse>> getSimilar(@PathVariable Long productId) {
        try {
            return ResponseEntity.ok(recommendationService.getSimilarProducts(productId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/low-carbon/{productId}")
    public ResponseEntity<List<RecommendationResponse>> getLowCarbon(@PathVariable Long productId) {
        try {
            return ResponseEntity.ok(recommendationService.getLowCarbonAlternatives(productId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
