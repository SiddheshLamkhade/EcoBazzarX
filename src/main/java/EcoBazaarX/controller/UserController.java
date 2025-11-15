package EcoBazaarX.controller;

import EcoBazaarX.entity.User;
import EcoBazaarX.entity.UserProfile;
import EcoBazaarX.repository.UserProfileRepository;
import EcoBazaarX.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile() {
        try {
            User user = authService.getCurrentUser();
            UserProfile profile = userProfileRepository.findByUser(user)
                    .orElseGet(() -> {
                        UserProfile newProfile = new UserProfile();
                        newProfile.setUser(user);
                        return userProfileRepository.save(newProfile);
                    });
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("phone", user.getPhone());
            response.put("role", user.getRole().name());
            response.put("carbonPoints", profile.getCarbonPoints());
            response.put("carbonSaved", profile.getCarbonSaved());
            response.put("badge", profile.getBadge());
            response.put("createdAt", user.getCreatedAt());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
