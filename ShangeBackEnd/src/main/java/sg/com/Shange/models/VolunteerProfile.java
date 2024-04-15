package sg.com.Shange.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerProfile {

    @NotEmpty(message = "Username is a mandatory field")
    @Size(min = 3, max = 20, message = "Username must be between 3 to 20 characters")
    private String userName;

    @NotEmpty(message = "Full Name is a mandatory field")
    @Size(min = 3, max = 20, message = "Full Name must be between 3 to 20 characters")
    private String fullName;

    private String userType;
    private String image;
    private String skills;
    private String availability;
    private String pastParticipation;

}

