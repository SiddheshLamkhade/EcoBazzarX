package EcoBazaarX.service;

import EcoBazaarX.dto.RecommendationResponse;
import EcoBazaarX.entity.Product;
import EcoBazaarX.entity.User;
import EcoBazaarX.entity.UserProfile;
import EcoBazaarX.repository.ProductRepository;
import EcoBazaarX.repository.UserProfileRepository;
import EcoBazaarX.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationService.class);
    private static final int MAX_RESULTS = 6;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    @Value("${openai.api.key:}")
    private String openAiApiKey;

    public RecommendationService(ProductRepository productRepository,
                                 UserRepository userRepository,
                                 UserProfileRepository userProfileRepository,
                                 ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public List<RecommendationResponse> getPersonalizedRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        UserProfile profile = userProfileRepository.findByUser(user).orElse(null);
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return List.of();
        }

        List<RecommendationCandidate> candidates = products.stream()
                .map(product -> new RecommendationCandidate(product, computePersonalizedScore(product, profile)))
                .sorted(Comparator.comparing(RecommendationCandidate::score).reversed())
                .limit(MAX_RESULTS)
                .toList();

        Map<Long, String> aiReasons = generateAiReasons(user, profile, candidates.stream()
                .map(RecommendationCandidate::product)
                .collect(Collectors.toList()));

        return candidates.stream()
                .map(candidate -> toResponse(candidate.product(), candidate.score(),
                        aiReasons.getOrDefault(candidate.product().getProductId(),
                                fallbackReason(candidate.product(), profile))))
                .toList();
    }

    public List<RecommendationResponse> getSimilarProducts(Long productId) {
        Product baseProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        List<Product> similar = Collections.emptyList();
        if (StringUtils.hasText(baseProduct.getCategory())) {
            similar = productRepository
                    .findTop8ByCategoryIgnoreCaseAndProductIdNotOrderByCarbonFootprintAsc(
                            baseProduct.getCategory(), baseProduct.getProductId());
        }

        if (similar.isEmpty()) {
            similar = productRepository.findTop8ByCarbonFootprintIsNotNullOrderByCarbonFootprintAsc();
        }

        return similar.stream()
                .map(product -> toResponse(product,
                        computeSimilarityScore(baseProduct, product),
                        buildSimilarReason(baseProduct, product)))
                .toList();
    }

    public List<RecommendationResponse> getLowCarbonAlternatives(Long productId) {
        Product baseProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        Double footprint = Optional.ofNullable(baseProduct.getCarbonFootprint()).orElse(Double.MAX_VALUE);
        List<Product> alternatives = productRepository
                .findTop8ByCarbonFootprintLessThanAndProductIdNotOrderByCarbonFootprintAsc(footprint, baseProduct.getProductId());

        if (alternatives.isEmpty()) {
            alternatives = productRepository.findTop8ByCarbonFootprintIsNotNullOrderByCarbonFootprintAsc();
        }

        return alternatives.stream()
                .map(product -> toResponse(product,
                        computeLowCarbonScore(baseProduct, product),
                        buildLowCarbonReason(baseProduct, product)))
                .toList();
    }

    private RecommendationResponse toResponse(Product product, double score, String reason) {
        return RecommendationResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .carbonFootprint(product.getCarbonFootprint())
                .imageUrl(product.getImageUrl())
                .score(Math.round(score * 100.0) / 100.0)
                .reason(reason)
                .build();
    }

    private double computePersonalizedScore(Product product, UserProfile profile) {
        double base = 50.0;
        double carbonScore = Optional.ofNullable(product.getCarbonFootprint())
                .map(footprint -> Math.max(0.0, 80.0 - footprint * 5))
                .orElse(40.0);

        double recencyBoost = 0.0;
        LocalDateTime created = product.getCreatedAt();
        if (created != null) {
            long days = Duration.between(created, LocalDateTime.now()).toDays();
            recencyBoost = Math.max(0, 20 - days);
        }

        double userBonus = Optional.ofNullable(profile)
                .map(UserProfile::getCarbonPoints)
                .map(points -> Math.min(15.0, points / 50.0))
                .orElse(0.0);

        return base + carbonScore + recencyBoost + userBonus;
    }

    private double computeSimilarityScore(Product base, Product other) {
        double score = 70.0;
        if (StringUtils.hasText(base.getCategory()) && base.getCategory().equalsIgnoreCase(other.getCategory())) {
            score += 15.0;
        }
        if (base.getPrice() != null && other.getPrice() != null) {
            double diff = Math.abs(base.getPrice() - other.getPrice());
            score += Math.max(0, 15 - diff);
        }
        return score;
    }

    private double computeLowCarbonScore(Product base, Product other) {
        double baseline = Optional.ofNullable(base.getCarbonFootprint()).orElse(40.0);
        double otherFootprint = Optional.ofNullable(other.getCarbonFootprint()).orElse(baseline);
        return 80 + Math.max(0, baseline - otherFootprint);
    }

    private String buildSimilarReason(Product base, Product other) {
        return String.format("Shares the %s category with %s while keeping price and footprint in a comparable range.",
                StringUtils.hasText(other.getCategory()) ? other.getCategory() : "same",
                base.getName());
    }

    private String buildLowCarbonReason(Product base, Product other) {
        if (base.getCarbonFootprint() == null || other.getCarbonFootprint() == null) {
            return "Lower estimated footprint alternative with strong sustainable sourcing.";
        }
        double delta = base.getCarbonFootprint() - other.getCarbonFootprint();
        return delta > 0
                ? String.format("Cuts roughly %.1f kg CO₂e compared with %s.", delta, base.getName())
                : "Consistently low-carbon option for eco-focused shoppers.";
    }

    private String fallbackReason(Product product, UserProfile profile) {
        String baseReason = "Low-carbon pick based on marketplace inventory.";
        if (product.getCarbonFootprint() != null) {
            baseReason = String.format("Estimated footprint %.1f kg CO₂e keeps it within eco targets.",
                    product.getCarbonFootprint());
        }
        if (profile != null && profile.getCarbonPoints() != null && profile.getCarbonPoints() > 0) {
            return baseReason + " Aligns with your sustainability progress.";
        }
        return baseReason;
    }

    private Map<Long, String> generateAiReasons(User user, UserProfile profile, List<Product> products) {
        if (products.isEmpty() || !StringUtils.hasText(openAiApiKey)) {
            return Collections.emptyMap();
        }

        try {
            String prompt = buildRecommendationPrompt(user, profile, products);
            String aiContent = callOpenAi(prompt);
            return parseAiReasons(aiContent);
        } catch (Exception e) {
            LOGGER.warn("OpenAI recommendation reasoning failed: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    private String buildRecommendationPrompt(User user, UserProfile profile, List<Product> products) {
        StringBuilder builder = new StringBuilder();
        builder.append("You are an eco-commerce assistant. Given the user context and product list, ")
                .append("return a JSON array where each item has productId and reason. ")
                .append("Keep reasons under 30 words.");

        builder.append("\nUser: ").append(user.getUsername());
        builder.append(" | Role: ").append(user.getRole());
        if (profile != null) {
            builder.append(" | CarbonPoints: ").append(profile.getCarbonPoints());
            builder.append(" | CarbonSaved: ").append(profile.getCarbonSaved());
        }

        builder.append("\nProducts:\n");
        for (Product product : products) {
            builder.append(String.format("- {\"productId\": %d, \"name\": \"%s\", \"category\": \"%s\", \"price\": %.2f, \"carbonFootprint\": %.2f}\n",
                    product.getProductId(),
                    product.getName(),
                    Optional.ofNullable(product.getCategory()).orElse("General"),
                    Optional.ofNullable(product.getPrice()).orElse(0.0),
                    Optional.ofNullable(product.getCarbonFootprint()).orElse(0.0)));
        }

        builder.append("\nReturn JSON only, e.g. [{\"productId\":1,\"reason\":\"text\"}].");
        return builder.toString();
    }

    private String callOpenAi(String prompt) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(
                Map.of("role", "system", "content", "You craft concise sustainability reasons."),
                Map.of("role", "user", "content", prompt)
        ));
        body.put("temperature", 0.5);
        body.put("max_tokens", 300);

        String response = webClient.post()
                .uri("/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse OpenAI response", e);
        }
    }

    private Map<Long, String> parseAiReasons(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            if (!node.isArray()) {
                return Collections.emptyMap();
            }
            Map<Long, String> reasons = new HashMap<>();
            for (JsonNode item : node) {
                JsonNode idNode = item.path("productId");
                JsonNode reasonNode = item.path("reason");
                if (idNode.isIntegralNumber() && reasonNode.isTextual()) {
                    reasons.put(idNode.asLong(), reasonNode.asText());
                }
            }
            return reasons;
        } catch (Exception e) {
            LOGGER.debug("Ignoring AI payload parsing issue: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    private record RecommendationCandidate(Product product, double score) {
    }
}
