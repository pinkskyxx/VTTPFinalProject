package sg.com.Shange.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDetails {

    private String strEventId;
    private String strUsername;
    private String strEventtype;
    private String strTitle;
    private String strDescription;
    private String strPhysical;
    private String strOnline;
    private String strNorth;
    private String strSouth;
    private String strEast;
    private String strWest;
    private String strCentral;
    private String strSampleFood;
    private String strSampleFoodSite1;
    private String strSampleFoodSite2;
    private String strQuiz;
    private String strImage;
    private String strFile;
    private String strEventFrom;
    private String strEventTo;
    private String strTimeFrom;
    private String strTimeTo;
    private String strVolunteer;
}
