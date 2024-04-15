package sg.com.Shange.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class allEvent {
    private int id;
    private int mysqlID;
    private String redisID;
    private String userName;
    private String address;
    private String title;
    private String description;
    private String eventDate;
    private String requestDate;
    private String creator;
    private String request;
    private String confirm;
}

