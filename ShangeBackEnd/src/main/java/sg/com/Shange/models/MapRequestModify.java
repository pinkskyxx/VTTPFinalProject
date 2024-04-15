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
public class MapRequestModify {
    
    private int id;
    private String address;
    private double locX;
    private double locY;
    private String myid;
    private String title;
    private String description;
    private Date eventDate;
    private Date requestDate;
    private String password;
    private boolean status;
    private String reply;

}
