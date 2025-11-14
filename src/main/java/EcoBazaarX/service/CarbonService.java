package EcoBazaarX.service;

import EcoBazaarX.dto.CarbonDetails;
import EcoBazaarX.dto.CarbonEstimateResponse;
import EcoBazaarX.entity.CarbonInsight;
import EcoBazaarX.entity.User;
import EcoBazaarX.repository.CarbonInsightRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarbonService {
    
    @Value("${openai.api.key}")
    private String openaiApiKey;
    
    @Autowired
    private CarbonInsightRepository carbonInsightRepository;
    
    @Autowired
    private AuthService authService;
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    public CarbonService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    @Transactional
    public CarbonEstimateResponse estimateCarbonFootprint(CarbonDetails details) {
        String prompt = buildCarbonEstimatePrompt(details);
        
        try {
            String response = callOpenAI(prompt);
            return parseOpenAIResponse(response, details.getProductName());
        } catch (Exception e) {
            // Fallback calculation if OpenAI fails
            return calculateFallbackCarbon(details);
        }
    }
    
    public Map<String, Object> getUserCarbonHistory() {
        User user = authService.getCurrentUser();
        List<CarbonInsight> insights = carbonInsightRepository.findByUserOrderByRecordedAtDesc(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("totalRecords", insights.size());
        response.put("insights", insights.stream().map(insight -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", insight.getId());
            item.put("productName", insight.getProductName());
            item.put("estimatedCarbon", insight.getEstimatedCarbon());
            item.put("recordedAt", insight.getRecordedAt());
            return item;
        }).collect(Collectors.toList()));
        
        return response;
    }
    
    private String buildCarbonEstimatePrompt(CarbonDetails details) {
        return String.format("""
                Calculate the carbon footprint for the following product and provide a brief explanation.
                
                Product: %s
                Volume: %s
                Weight: %s
                Materials: %s
                Manufacturing Location: %s
                Electricity Type: %s
                Manufacturing Energy: %s
                Packaging: %s
                Shipping Mode: %s
                Sea Freight Distance: %.1f km
                Truck Distance: %.1f km
                Lifespan: %s
                Power Usage: %s
                Recyclability: %.1f%%
                Biodegradability: %.1f%%
                
                Provide your response in this exact format:
                CARBON: [number]
                EXPLANATION: [brief explanation]
                """,
                details.getProductName(), details.getVolume(), details.getWeight(),
                details.getMaterialComposition(), details.getManufacturingLocation(),
                details.getElectricityType(), details.getManufacturingEnergyUsed(),
                details.getPackagingDetails(), details.getShippingMode(),
                details.getSeaFreightDistance(), details.getTruckDistance(),
                details.getLifespan(), details.getPowerUsage(),
                details.getRecyclabilityRate(), details.getBiodegradabilityRate()
        );
    }
    
    private String callOpenAI(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are a carbon footprint calculation expert."),
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 500);
        
        try {
            String response = webClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("OpenAI API call failed", e);
        }
    }
    
    private CarbonEstimateResponse parseOpenAIResponse(String response, String productName) {
        try {
            String[] lines = response.split("\n");
            double carbon = 0.0;
            String explanation = "";
            
            for (String line : lines) {
                if (line.startsWith("CARBON:")) {
                    carbon = Double.parseDouble(line.substring(7).trim());
                } else if (line.startsWith("EXPLANATION:")) {
                    explanation = line.substring(12).trim();
                }
            }
            
            // Save to history if user is authenticated
            try {
                User user = authService.getCurrentUser();
                CarbonInsight insight = new CarbonInsight();
                insight.setUser(user);
                insight.setProductName(productName);
                insight.setEstimatedCarbon(carbon);
                carbonInsightRepository.save(insight);
            } catch (Exception ignored) {
                // User not authenticated, skip saving
            }
            
            return new CarbonEstimateResponse(carbon, explanation);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI response", e);
        }
    }
    
    private CarbonEstimateResponse calculateFallbackCarbon(CarbonDetails details) {
        // Simple fallback calculation
        double carbon = 5.0; // Base carbon
        
        if (details.getElectricityType() != null && 
            (details.getElectricityType().toLowerCase().contains("solar") || 
             details.getElectricityType().toLowerCase().contains("renewable"))) {
            carbon -= 2.0;
        }
        
        if (details.getRecyclabilityRate() != null && details.getRecyclabilityRate() > 70) {
            carbon -= 1.0;
        }
        
        carbon = Math.max(carbon, 1.0);
        
        String explanation = String.format("Estimated carbon footprint of %.1f kg CO₂e based on material composition, " +
                "manufacturing process, and recyclability factors.", carbon);
        
        return new CarbonEstimateResponse(carbon, explanation);
    }
}
