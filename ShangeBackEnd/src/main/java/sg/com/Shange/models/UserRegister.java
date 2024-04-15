package sg.com.Shange.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {

    @NotEmpty(message = "Username is a mandatory field")
    @Size(min = 3, max = 20, message = "Username must be between 3 to 20 characters")
    String userName;

    @NotEmpty(message = "Password is a mandatory field")
    @Size(min = 3, max = 20, message = "Password must be between 3 to 20 characters")
    String password;

    @NotEmpty(message = "Full Name is a mandatory field")
    @Size(min = 3, max = 20, message = "Full Name must be between 3 to 20 characters")
    String fullName;

    @NotEmpty(message = "Address is a mandatory field")
    @Size(min = 3, max = 150, message = "Location Address must be between 3 to 50 characters")
    String address;

    private Date dateOfBirth;

    @Email(message = "Invalid Email Format")
    @Size(max = 30, message = "Email length exceeded 30 characters")
    @NotBlank(message = "Email is a mandatory field")
    private String email;

    @Pattern(regexp = "(6|8|9)[0-9]{7}", message = "Invalid phone number entered")
    private String phoneNo;

    private Date dateOfSignup;
    String gender;
    String type;

}