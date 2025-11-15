package EcoBazaarX.config;

import EcoBazaarX.entity.User;
import EcoBazaarX.entity.UserProfile;
import EcoBazaarX.repository.UserProfileRepository;
import EcoBazaarX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        // Check if admin user exists by username
        if (userRepository.existsByUsername("admin")) {
            // Admin exists - update password
            User existingAdmin = userRepository.findByUsername("admin").orElse(null);
            if (existingAdmin != null) {
                existingAdmin.setPassword(passwordEncoder.encode("EcoAdmin@2024"));
                userRepository.save(existingAdmin);
                System.out.println("✅ Admin account found - password reset to: EcoAdmin@2024");
                System.out.println("   Username: " + existingAdmin.getUsername());
                System.out.println("   Email: " + existingAdmin.getEmail());
            }
        } else if (!userRepository.existsByEmail("admin@ecobazaarx.com")) {
            // Create default admin user if it doesn't exist
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("EcoAdmin@2024"));
            admin.setEmail("admin@ecobazaarx.com");
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setPhone("9999999999");
            admin.setRole(User.Role.ADMIN);
            
            admin = userRepository.save(admin);
            
            // Create user profile for leaderboard
            UserProfile profile = new UserProfile();
            profile.setUser(admin);
            userProfileRepository.save(profile);
            
            System.out.println("✅ Default admin account created successfully!");
            System.out.println("   Email: admin@ecobazaarx.com");
            System.out.println("   Password: EcoAdmin@2024");
        }
    }
}
