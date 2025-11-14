package EcoBazaarX.controller;

import EcoBazaarX.dto.CarbonEstimateRequest;
import EcoBazaarX.dto.CarbonEstimateResponse;
import EcoBazaarX.service.CarbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/carbon")
@CrossOrigin(origins = "*")
public class CarbonController {
    
    @Autowired
    private CarbonService carbonService;
    
    @PostMapping("/estimate")
    public ResponseEntity<CarbonEstimateResponse> estimateCarbonFootprint(
            @RequestBody CarbonEstimateRequest request) {
        try {
            CarbonEstimateResponse response = carbonService.estimateCarbonFootprint(request.getCarbonDetails());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/user/history")
    public ResponseEntity<Map<String, Object>> getUserCarbonHistory() {
        try {
            Map<String, Object> history = carbonService.getUserCarbonHistory();
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
