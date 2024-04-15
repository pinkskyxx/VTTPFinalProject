package sg.com.Shange.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEvent {
    private String id;
    private String summary;
    private String description;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
}
