package EcoBazaarX.service;

import EcoBazaarX.entity.UserProfile;
import EcoBazaarX.entity.User;
import EcoBazaarX.repository.UserProfileRepository;
import EcoBazaarX.repository.UserRepository;
import EcoBazaarX.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    public List<Map<String, Object>> getTopEcoUsers() {
        List<UserProfile> profiles = userProfileRepository.findAllByOrderByCarbonPointsDesc();
        return profiles.stream()
                .limit(10)
                .map(this::mapProfileToResponse)
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getUserRank(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        
        List<UserProfile> allProfiles = userProfileRepository.findAllByOrderByCarbonPointsDesc();
        int rank = allProfiles.indexOf(profile) + 1;
        
        Map<String, Object> response = new HashMap<>();
        response.put("rank", rank);
        response.put("carbonPoints", profile.getCarbonPoints());
        response.put("carbonSaved", profile.getCarbonSaved());
        response.put("badge", profile.getBadge());
        response.put("username", user.getUsername());
        
        return response;
    }
    
    public List<Map<String, Object>> getTopByPoints() {
        return userProfileRepository.findAllByOrderByCarbonPointsDesc().stream()
                .limit(10)
                .map(profile -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", profile.getId());
                    map.put("carbonPoints", profile.getCarbonPoints());
                    map.put("carbonSaved", profile.getCarbonSaved());
                    map.put("badge", profile.getBadge());
                    return map;
                })
                .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getTopByCarbon() {
        return userProfileRepository.findAllByOrderByCarbonSavedDesc().stream()
                .limit(10)
                .map(profile -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", profile.getId());
                    map.put("carbonPoints", profile.getCarbonPoints());
                    map.put("carbonSaved", profile.getCarbonSaved());
                    map.put("badge", profile.getBadge());
                    return map;
                })
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getUserStats(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        
        List<UserProfile> allProfiles = userProfileRepository.findAllByOrderByCarbonPointsDesc();
        int rank = allProfiles.indexOf(profile) + 1;
        
        long totalOrders = orderRepository.findByUserOrderByCreatedAtDesc(user).size();
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("rank", rank);
        response.put("carbonPoints", profile.getCarbonPoints());
        response.put("carbonSaved", profile.getCarbonSaved());
        response.put("badge", profile.getBadge());
        response.put("totalOrders", totalOrders);
        
        return response;
    }
    
    private Map<String, Object> mapProfileToResponse(UserProfile profile) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", profile.getId());
        
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", profile.getUser().getUsername());
        userMap.put("firstName", profile.getUser().getFirstName());
        userMap.put("lastName", profile.getUser().getLastName());
        map.put("user", userMap);
        
        map.put("carbonPoints", profile.getCarbonPoints());
        map.put("carbonSaved", profile.getCarbonSaved());
        map.put("badge", profile.getBadge());
        
        return map;
    }
}
