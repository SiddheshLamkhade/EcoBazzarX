package EcoBazaarX.controller;

import EcoBazaarX.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leaderboard")
@CrossOrigin(origins = "*")
public class LeaderboardController {
    
    @Autowired
    private LeaderboardService leaderboardService;
    
    @GetMapping("/top")
    public ResponseEntity<List<Map<String, Object>>> getTopEcoUsers() {
        return ResponseEntity.ok(leaderboardService.getTopEcoUsers());
    }
    
    @GetMapping("/rank/{userId}")
    public ResponseEntity<Map<String, Object>> getUserRank(@PathVariable Long userId) {
        try {
            Map<String, Object> rank = leaderboardService.getUserRank(userId);
            return ResponseEntity.ok(rank);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/top-points")
    public ResponseEntity<List<Map<String, Object>>> getTopByPoints() {
        return ResponseEntity.ok(leaderboardService.getTopByPoints());
    }
    
    @GetMapping("/top-carbon")
    public ResponseEntity<List<Map<String, Object>>> getTopByCarbon() {
        return ResponseEntity.ok(leaderboardService.getTopByCarbon());
    }
    
    @GetMapping("/user/{username}")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable String username) {
        try {
            Map<String, Object> stats = leaderboardService.getUserStats(username);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
