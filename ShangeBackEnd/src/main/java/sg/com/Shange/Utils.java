package sg.com.Shange;

import jakarta.json.JsonObject;
import jakarta.json.Json;


public class Utils {
    public static final String REDIS_USER = "redis_user";
    public static final String REDIS_NewUSER = "redis_newUser";

    public static final String REDIS_Volunteer = "Volunteer";
    public static final String REDIS_Food = "Food";
    public static final String REDIS_EventDetails = "EventDetails";
    public static final String REDIS_EventUser = "EventUser";
    public static final String REDIS_VolunteerProfile = "VolunteerProfile";
    public static final String REDIS_OrganisationProfile = "OrganisationProfile";

    public static final String Sess_name = "sessionName";
    public static final String Sess_EventDetails = "sessionEventDetails";
    public static final String Sess_EventFoodDetails = "sessionEventFoodDetails";
    public static final String Sess_VolunteerProfile = "sessionVolunteerProfiles";
    public static final String Sess_OrganisationProfile = "sessionOrganisationProfile";
    public static final String Sess_Profile = "sessionProfile";
    public static final String Sess_Profile_volunteer_joint = "sessionProfileVolunteerJoint";
    public static final String Sess_Profile_volunteer = "sessionProfileVolunteer";
    public static final String Sess_SearchVolunteer = "sessionVolunteerSearch";
    public static final String Sess_SearchVolunteerRequestRedis = "sessionVolunteerSearchRedis";
    public static final String Sess_SearchVolunteerRequestSQL = "sessionVolunteerSearchSQL";
    public static final String Sess_SearchVolunteerRequestStatus = "sessionVolunteerSearchStatus";
    public static final String Sess_SearchVolunteerMapRequestStatus = "sessionVolunteerMapSearchStatus";
    public static final String Sess_SearchVolunteerMapConfirmStatus = "sessionVolunteerMapConfirmStatus";
    public static final String Sess_SearchOrganisation = "sessionOrganisationSearch";
    public static final String Sess_GoogleCalendar_login = "sessionGoogleCalendar";
    public static final String Sess_GoogleCalendar_details = "sessionGoogleCalendarDetails";


        public static JsonObject returnNewsId(String newsId) {
        return Json.createObjectBuilder()
                .add("newsId", newsId)
                .build();
    }

}
