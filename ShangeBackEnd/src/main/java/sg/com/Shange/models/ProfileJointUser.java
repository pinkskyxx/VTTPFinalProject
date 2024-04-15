package sg.com.Shange.models;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileJointUser {
        @NotEmpty(message = "Username is a mandatory field")
    @Size(min = 3, max = 20, message = "Username must be between 3 to 20 characters")
    private String userName;

    @NotEmpty(message = "Full Name is a mandatory field")
    @Size(min = 3, max = 20, message = "Full Name must be between 3 to 20 characters")
    private String fullName;
    private String image;
    private String skills;
    private String availability;
    private String pastParticipation;
    
    @Pattern(regexp = "(6|8|9)[0-9]{7}", message = "Invalid phone number entered")
    private String phoneNo;

    private String gender;
    private String type;

    @Email(message = "Invalid Email Format")
    @Size(max = 30, message = "Email length exceeded 30 characters")
    @NotBlank(message = "Email is a mandatory field")
    private String email;

}
