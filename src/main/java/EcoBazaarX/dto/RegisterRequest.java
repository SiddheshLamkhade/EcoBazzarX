package EcoBazaarX.dto;

import EcoBazaarX.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private User.Role role = User.Role.USER;
    private String businessName;
    private String businessType;
    private String gstNumber;
    private String website;
    private String description;
    private List<String> documents;
}
