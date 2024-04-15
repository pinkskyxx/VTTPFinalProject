package sg.com.Shange.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin implements Serializable{

     private String userName;
     private String userPassword;
     private String userType;
}
