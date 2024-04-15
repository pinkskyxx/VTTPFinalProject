package sg.com.Shange.models;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    private String userName;
   
    private String image;
    @Size(min = 0, max = 20, message = "Skill must be between 3 to 64 characters")
    private String skills;

    @Size(min = 0, max = 20, message = "availability must be between 3 to 64 characters")
    private String availability;

    @Size(min = 0, max = 20, message = "pastParticipation must be between 3 to 128 characters")
    private String pastParticipation;

    private String clientId;
    private String clientSecret;
}
