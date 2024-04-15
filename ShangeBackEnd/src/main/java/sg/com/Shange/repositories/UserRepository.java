package sg.com.Shange.repositories;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import sg.com.Shange.models.MapRequest;
import sg.com.Shange.models.MapRequestModify;
import sg.com.Shange.models.ProfileJointUser;
import sg.com.Shange.models.UserLogin;
import sg.com.Shange.models.UserProfile;
import sg.com.Shange.models.UserRegister;
import sg.com.Shange.models.allEvent;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate template;

    public static final String SQL_INSERT_NEW_USER = """
            insert into user_init(userName, password, fullName, address, dateOfBirth,
                                email, phoneNo, dateOfSignup, gender, type)
                values (?, ?, ?,?, ?, ?,?, ?, ?,?)
            """;

    public boolean insertNewUser(UserRegister userItems)
            throws User_Init_Exception {

        int inserted = template.update(SQL_INSERT_NEW_USER,
                userItems.getUserName(),
                userItems.getPassword(),
                userItems.getFullName(),
                userItems.getAddress(),
                userItems.getDateOfBirth(),
                userItems.getEmail(),
                userItems.getPhoneNo(),
                userItems.getDateOfSignup(),
                userItems.getGender(),
                userItems.getType());

        System.out.printf("--- inserted: %d\n", inserted);
        return inserted > 0;

    }

    public static final String SQL_INSERT_NEW_USER_LOGIN = """
            insert into login_init(userName, password, type)
                values (?, ?, ?)
            """;

    public boolean insertNewUserLogin(UserRegister userItems)
            throws User_Init_Exception {

        int inserted = template.update(SQL_INSERT_NEW_USER_LOGIN,
                userItems.getUserName(),
                userItems.getPassword(),
                userItems.getType());

        System.out.printf("--- inserted Login Details: %d\n", inserted);
        return inserted > 0;

    }

    public static final String SQL_SELECT_USER_BY_USERNAME = """
            select * from user_init where userName = ?
            """;

    public boolean CheckUserExists(String userName) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME, userName);
        if (!rs.next())
            return false;

        return true;
    }

    public UserRegister GetUserRegisteredDetails(String userName) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME, userName);
        UserRegister b = new UserRegister();
        if (rs.next()) {
            b.setUserName(rs.getString("userName"));
            b.setPassword(rs.getString("password"));
            b.setFullName(rs.getString("fullName"));
            b.setAddress(rs.getString("address"));
            b.setDateOfBirth(rs.getTimestamp("dateOfBirth"));
            b.setEmail(rs.getString("email"));
            b.setPhoneNo(rs.getString("phoneNo"));
            b.setDateOfSignup(rs.getTimestamp("dateOfSignup"));
            b.setGender(rs.getString("gender"));
            b.setType(rs.getString("type"));
            return b;
        }
        return b;
    }

    public static final String SQL_SELECT_ALL_USER = """
            select * from login_init
            """;

    public List<UserLogin> GetAllLoginUser() {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ALL_USER);
        List<UserLogin> userLogins = new LinkedList<>();
        while (rs.next()) {
            UserLogin b = new UserLogin();
            b.setUserName(rs.getString("userName"));
            b.setUserPassword(rs.getString("password"));
            b.setUserType(rs.getString("type"));

            userLogins.add(b);
        }
        return userLogins;
    }

    public static final String SQL_SELECT_PROFILE_USERNAME = """
            select * from profile where userName = ?
            """;

    public boolean CheckProfileExists(String userName) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_PROFILE_USERNAME, userName);
        if (!rs.next())
            return false;

        return true;
    }

    public UserProfile GetUserProfile(String userName) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_PROFILE_USERNAME, userName);
        UserProfile b = new UserProfile();
        if (rs.next()) {
            b.setUserName(rs.getString("userName"));
            b.setImage(rs.getString("image"));
            b.setSkills(rs.getString("skills"));
            b.setAvailability(rs.getString("availability"));
            b.setPastParticipation(rs.getString("pastParticipation"));
            b.setClientId(rs.getString("clientId"));
            b.setClientSecret(rs.getString("clientSecret"));
            return b;
        }
        return b;
    }

    public static final String SQL_SELECT_PROFILE_ALL = """
            select * from profile
            """;

    public List<UserProfile> GetAllProfile() {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_PROFILE_ALL);
        List<UserProfile> userProfile = new LinkedList<>();

        while (rs.next()) {
            UserProfile b = new UserProfile();
            b.setUserName(rs.getString("userName"));
            b.setImage(rs.getString("image"));
            b.setSkills(rs.getString("skills"));
            b.setAvailability(rs.getString("availability"));
            b.setPastParticipation(rs.getString("pastParticipation"));
            b.setClientId(rs.getString("clientId"));
            b.setClientSecret(rs.getString("clientSecret"));
            userProfile.add(b);
        }
        return userProfile;
    }

    public static final String SQL_SELECT_PROFILE_ALL_JOINT = """
                SELECT
                p.userName,
                p.image,
                p.skills,
                p.availability,
                p.pastParticipation,
                u.fullName,
                u.type,
                u.gender,
                u.email,
                u.phoneNo
            FROM
                profile p
            JOIN
                user_init u ON p.userName = u.userName
            WHERE
                u.type = ?
                    """;

    public List<ProfileJointUser> GetAllProfileJoint(String type) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_PROFILE_ALL_JOINT, type);
        List<ProfileJointUser> userProfile = new LinkedList<>();

        while (rs.next()) {
            ProfileJointUser b = new ProfileJointUser();
            b.setUserName(rs.getString("userName"));
            b.setType(rs.getString("type"));
            b.setImage(rs.getString("image"));
            b.setSkills(rs.getString("skills"));
            b.setAvailability(rs.getString("availability"));
            b.setPastParticipation(rs.getString("pastParticipation"));
            b.setFullName(rs.getString("fullName"));
            b.setEmail(rs.getString("email"));
            b.setPhoneNo(rs.getString("phoneNo"));
            b.setGender(rs.getString("gender"));
            userProfile.add(b);
        }
        return userProfile;
    }

    public static final String SQL_SELECT_PROFILE_ALL_TYPE = """
            select * from profile where type = ?
            """;

    public List<UserProfile> GetAllProfileType(String type) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_PROFILE_ALL_TYPE, type);
        List<UserProfile> userProfile = new LinkedList<>();

        while (rs.next()) {
            UserProfile b = new UserProfile();
            b.setUserName(rs.getString("userName"));
            // b.setType(rs.getString("type"));
            b.setImage(rs.getString("image"));
            b.setSkills(rs.getString("skills"));
            b.setAvailability(rs.getString("availability"));
            b.setPastParticipation(rs.getString("pastParticipation"));
            b.setClientId(rs.getString("clientId"));
            b.setClientSecret(rs.getString("clientSecret"));
            userProfile.add(b);
        }
        return userProfile;
    }

    public static final String SQL_INSERT_PROFILE = """
            insert into profile(userName, image, skills,availability,pastParticipation,clientId,clientSecret)
            values (?, ?, ?, ?, ?, ?, ?)
            """;

    public boolean insertProfile(UserProfile userItems)
            throws User_Init_Exception {

        int inserted = template.update(SQL_INSERT_PROFILE,
                userItems.getUserName(),
                // userItems.getType(),
                userItems.getImage(),
                userItems.getSkills(),
                userItems.getAvailability(),
                userItems.getPastParticipation(),
                userItems.getClientId(),
                userItems.getClientSecret());

        System.out.printf("--- inserted profile Details: %d\n", inserted);
        return inserted > 0;

    }

    public static final String SQL_UPDATE_PROFILE = """
            update profile
            set image = ?,
            skills = ?,
            availability = ?,
            pastParticipation = ?,
            clientId = ?,
            clientSecret = ?
            where userName = ?
            """;

    public boolean UpdateProfile(UserProfile userItems)
            throws User_Init_Exception {

        int inserted = template.update(SQL_UPDATE_PROFILE,
                userItems.getImage(),
                userItems.getSkills(),
                userItems.getAvailability(),
                userItems.getPastParticipation(),
                userItems.getClientId(),
                userItems.getClientSecret(),
                userItems.getUserName());

        System.out.printf("--- update profile Details: %d\n", inserted);
        return inserted > 0;

    }

    // Map Event Request
    public static final String SQL_INSERT_NEW_MAP_EVENT_REQUEST = """
            insert into mapEventRequest(address, locX, locY, myid, title,
            description, eventDate, requestDate, password, status, reply)
                values (?,?,?,?,?, ?,?,?,?,?, ?)
            """;

    public boolean insertNewMapEventRequest(MapRequest Items)
            throws User_Init_Exception {

        int inserted = template.update(SQL_INSERT_NEW_MAP_EVENT_REQUEST,
                Items.getAddress(),
                Items.getLocX(),
                Items.getLocY(),
                Items.getMyid(),
                Items.getTitle(),
                Items.getDescription(),
                Items.getEventDate(),
                Items.getRequestDate(),
                Items.getPassword(),
                Items.isStatus(), Items.getReply());

        System.out.printf("--- inserted: %d\n", inserted);
        return inserted > 0;

    }

    public static final String SQL_SELECT_MODIFY_BY_DATE_RANGE = """
            select * from mapEventRequest where requestDate between ? and ?
            """;

    public List<MapRequestModify> allMapEventModify(Date dateFrom, Date dateTo) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_MODIFY_BY_DATE_RANGE, dateFrom, dateTo);
        List<MapRequestModify> maprequestmodify = new LinkedList<>();
        while (rs.next()) {
            MapRequestModify b = new MapRequestModify();
            b.setId(rs.getInt("id"));
            b.setAddress(rs.getString("address"));
            b.setLocX(rs.getDouble("locX"));
            b.setLocY(rs.getDouble("locY"));
            b.setMyid(rs.getString("myid"));
            b.setTitle(rs.getString("title"));
            b.setDescription(rs.getString("description"));
            b.setEventDate(rs.getTimestamp("eventDate"));
            b.setRequestDate(rs.getTimestamp("requestDate"));
            b.setPassword(rs.getString("password"));
            b.setStatus(rs.getBoolean("status"));
            b.setReply(rs.getString("reply"));
            
            maprequestmodify.add(b);
        }
        return maprequestmodify;
    }

    public static final String SQL_SELECT_MODIFY_BY_DATE_RANGE_ED = """
            select * from mapEventRequest where eventDate between ? and ?
            """;

    public List<MapRequestModify> allMapEventModifyeventDate(Date dateFrom, Date dateTo) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_MODIFY_BY_DATE_RANGE_ED, dateFrom, dateTo);
        List<MapRequestModify> maprequestmodify = new LinkedList<>();
        while (rs.next()) {
            MapRequestModify b = new MapRequestModify();
            b.setId(rs.getInt("id"));
            b.setAddress(rs.getString("address"));
            b.setLocX(rs.getDouble("locX"));
            b.setLocY(rs.getDouble("locY"));
            b.setMyid(rs.getString("myid"));
            b.setTitle(rs.getString("title"));
            b.setDescription(rs.getString("description"));
            b.setEventDate(rs.getTimestamp("eventDate"));
            b.setRequestDate(rs.getTimestamp("requestDate"));
            b.setPassword(rs.getString("password"));
            b.setStatus(rs.getBoolean("status"));
            b.setReply(rs.getString("reply"));
            maprequestmodify.add(b);
        }
        return maprequestmodify;
    }

    public static final String SQL_SELECT_MODIFY_ALL = """
            select * from mapEventRequest
            """;

    public List<MapRequestModify> GetallMapEventModify() {
        System.out.println("Resp get all");
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_MODIFY_ALL);
        List<MapRequestModify> maprequestmodify = new LinkedList<>();
        while (rs.next()) {
            MapRequestModify b = new MapRequestModify();
            b.setId(rs.getInt("id"));
            b.setAddress(rs.getString("address"));
            b.setLocX(rs.getDouble("locX"));
            b.setLocY(rs.getDouble("locY"));
            b.setMyid(rs.getString("myid"));
            b.setTitle(rs.getString("title"));
            b.setDescription(rs.getString("description"));
            b.setEventDate(rs.getTimestamp("eventDate"));
            b.setRequestDate(rs.getTimestamp("requestDate"));
            b.setPassword(rs.getString("password"));
            b.setStatus(rs.getBoolean("status"));
            b.setReply(rs.getString("reply"));
            maprequestmodify.add(b);
        }
        return maprequestmodify;
    }

    public static final String SQL_SELECT_id_BY_MYID_AND_TITLE = """
            select id from mapEventRequest where myid = ? and title = ?
            """;

    public int getMapEventId(String myid, String title) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_id_BY_MYID_AND_TITLE, myid, title);
        int sqlid = -1;
        if (rs.next()) {
            MapRequestModify b = new MapRequestModify();
            sqlid = rs.getInt("id");

        }
        return sqlid;
    }

    public static final String SQL_SELECT_REPLY_BY_ID = """
            select reply from mapEventRequest where id = ?
            """;

    public String getMapEventReplyById(int id) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_REPLY_BY_ID, id);
        String reply = "";
        if (rs.next()) {
            reply = rs.getString("reply");

        }
        System.out.println("reply is: "+reply);
        return reply;
    }

    public static final String SQL_DELETE_MODIFY_BY_ID = """
            delete from mapEventRequest where id = ?
            """;

    public boolean deleteMapEventModifyById(int id) {
        int result = template.update(SQL_DELETE_MODIFY_BY_ID, id);
        System.out.printf("--- delete: %d\n", result);
        return result > 0;
    }

    public static final String SQL_UPDATE_MODIFY_BY_ID = """
            update mapEventRequest
            set address = ?,
             locX = ?,
             locY = ?,
             title = ?,
             description = ?,
             eventDate = ?,
             reply = ?
            where id = ?
            """;

    public boolean updateMapEventModifyById(int id, MapRequestModify data) {
        int result = template.update(SQL_UPDATE_MODIFY_BY_ID,
                data.getAddress(), data.getLocX(), data.getLocY(), data.getTitle(),
                data.getDescription(), data.getEventDate(), data.getReply(), data.getId());
        System.out.printf("--- update: %d\n", result);
        return result > 0;
    }

    public static final String SQL_UPDATE_MODIFY_REPLY_BY_ID = """
            update mapEventRequest
            set reply = ?,
                status = ? 
            where id = ?
            """;

    public boolean updateMapEventModifyReplyById(int id, String reply) {
        int result = template.update(SQL_UPDATE_MODIFY_REPLY_BY_ID,
                reply, 1, id);
        System.out.printf("--- update: %d %s\n", id, reply);
        return result > 0;
    }

    public static final String SQL_INSERT_REDIS_NEW_USER_EVENT = """
            insert into allEvent(redisID, userName, address, title, description, eventDate, requestDate, creator)
                values (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public boolean insertRdisNewUserEvent(allEvent Items)
            throws User_Init_Exception {

        int inserted = template.update(SQL_INSERT_REDIS_NEW_USER_EVENT,
                Items.getRedisID(),
                Items.getUserName(),
                Items.getAddress(),
                Items.getTitle(),
                Items.getDescription(),
                Items.getEventDate(),
                Items.getRequestDate(), Items.getUserName());

        System.out.printf("--- inserted Redis event Details: %d\n", inserted);
        return inserted > 0;
    }

    public static final String SQL_INSERT_SQL_NEW_USER_EVENT = """
            insert into allEvent(mysqlID, userName, title, description, eventDate, requestDate, creator)
            values (?, ?, ?, ?, ?, ?, ?)
                """;

    public boolean insertSQLNewUserEvent(allEvent Items)
            throws User_Init_Exception {
        int inserted = template.update(SQL_INSERT_SQL_NEW_USER_EVENT,
                Items.getMysqlID(),
                Items.getUserName(),
                Items.getTitle(),
                Items.getDescription(),
                Items.getEventDate(),
                Items.getRequestDate(), Items.getUserName());

        System.out.printf("--- inserted SQL event Details: %d\n", inserted);
        return inserted > 0;
    }

    public boolean insertSQLNewUserEvent1(allEvent Items)
            throws User_Init_Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date eventDate = null;
        Date requestDate = null;
        try {
            eventDate = dateFormat.parse(Items.getEventDate());
            requestDate = dateFormat.parse(Items.getRequestDate());
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing exception
        }
        Timestamp eventTimestamp = new Timestamp(eventDate.getTime());
        Timestamp requestTimestamp = new Timestamp(requestDate.getTime());
        System.out.println(eventTimestamp);
        System.out.println(requestTimestamp);

        int inserted = template.update(SQL_INSERT_SQL_NEW_USER_EVENT,
                Items.getMysqlID(),
                Items.getUserName(),
                Items.getTitle(),
                Items.getDescription(),
                eventTimestamp,
                requestTimestamp, Items.getUserName());

        System.out.printf("--- inserted SQL event Details: %d\n", inserted);
        return inserted > 0;
    }

    public static final String SQL_INSERT_REDIS_NEW_USER_EVENT_REQUEST = """
            insert into allEvent(redisID, userName, address, title, description, eventDate, requestDate, request)
                values (?, ?, ?, ?, ?, ?, ?, ?)
            """;

    public boolean insertRdisNewUserEventRequest(allEvent Items)
            throws User_Init_Exception {

        int inserted = template.update(SQL_INSERT_REDIS_NEW_USER_EVENT_REQUEST,
                Items.getRedisID(),
                Items.getUserName(),
                Items.getAddress(),
                Items.getTitle(),
                Items.getDescription(),
                Items.getEventDate(),
                Items.getRequestDate(), Items.getRequest());

        System.out.printf("--- inserted Redis event Request Details: %d\n", inserted);
        return inserted > 0;
    }

    public static final String SQL_INSERT_SQL_NEW_USER_EVENT_REQUEST = """
            insert into allEvent(mysqlID, userName, title, description, eventDate, requestDate, request)
                values (?, ?, ?, ?, ?, ?, ?)
            """;

    public boolean insertSQLNewUserEventRequest(allEvent Items)
            throws User_Init_Exception {

        int inserted = template.update(SQL_INSERT_SQL_NEW_USER_EVENT_REQUEST,
                Items.getMysqlID(),
                Items.getUserName(),
                Items.getTitle(),
                Items.getDescription(),
                Items.getEventDate(),
                Items.getRequestDate(), Items.getUserName());

        System.out.printf("--- inserted SQL event Request Details: %d\n", inserted);
        return inserted > 0;
    }

    public static final String SQL_SELECT_NEWEVENT_REQUEST_CHECK_REDIS = """
            select * from allEvent where request = ? and redisID = ?
            """;

    public boolean CheckNewEventRequestRedisExists(String username, String redisid) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_NEWEVENT_REQUEST_CHECK_REDIS, username, redisid);
        if (!rs.next())
            return false;

        return true;
    }
    public static final String SQL_SELECT_NEWEVENT_REQUEST_CHECK_REDIS_COMFIRM = """
        select * from allEvent where  userName = ? and request <> ''
        """;

    public List<allEvent> getNewEventRequestRedisExistsConfirm(String username) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_NEWEVENT_REQUEST_CHECK_REDIS_COMFIRM, username);
        List<allEvent> event = new LinkedList<>();

        while (rs.next()) {
            allEvent b = new allEvent();
            b.setUserName(rs.getString("userName"));
            b.setAddress(rs.getString("address"));
            b.setConfirm(rs.getString("confirm"));
            b.setCreator(rs.getString("creator"));
            b.setEventDate(rs.getString("eventDate"));
            b.setId(rs.getInt("id"));
            b.setMysqlID(rs.getInt("mysqlID"));
            b.setRedisID(rs.getString("redisID"));
            b.setRequest(rs.getString("request"));
            b.setRequestDate(rs.getString("requestDate"));
            b.setTitle(rs.getString("title"));
            b.setDescription(rs.getString("description"));

            event.add(b);
        }
        return event;
    }

    public static final String SQL_SELECT_NEWEVENT_REQUEST_CHECK_SQL = """
            select * from allEvent where request = ? and mysqlID = ?
            """;

    public boolean CheckNewEventRequestSqlExists(String username, int sqlid) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_NEWEVENT_REQUEST_CHECK_SQL, username, sqlid);
        if (!rs.next())
            return false;

        return true;
    }

    public static final String SQL_SELECT_NEW_USER_EVENT_OWN_REQUEST = """
            select * from allEvent where request = ?
            """;

    public List<allEvent> GetAllNewEventOwnRequest(String username) {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_NEW_USER_EVENT_OWN_REQUEST, username);
        List<allEvent> event = new LinkedList<>();

        while (rs.next()) {
            allEvent b = new allEvent();
            b.setUserName(rs.getString("userName"));
            b.setAddress(rs.getString("address"));
            b.setConfirm(rs.getString("confirm"));
            b.setCreator(rs.getString("creator"));
            b.setEventDate(rs.getString("eventDate"));
            b.setId(rs.getInt("id"));
            b.setMysqlID(rs.getInt("mysqlID"));
            b.setRedisID(rs.getString("redisID"));
            b.setRequest(rs.getString("request"));
            b.setRequestDate(rs.getString("requestDate"));
            b.setTitle(rs.getString("title"));
            b.setDescription(rs.getString("description"));

            event.add(b);
        }
        return event;
    }

    public static final String SQL_SELECT_NEW_USER_EVENT_REQUEST = """
            select * from allEvent where request <> ''
            """;

    public List<allEvent> GetAllNewEventRequest() {
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_NEW_USER_EVENT_REQUEST);
        List<allEvent> event = new LinkedList<>();

        while (rs.next()) {
            allEvent b = new allEvent();
            b.setUserName(rs.getString("userName"));
            b.setAddress(rs.getString("address"));
            b.setConfirm(rs.getString("confirm"));
            b.setCreator(rs.getString("creator"));
            b.setEventDate(rs.getString("eventDate"));
            b.setId(rs.getInt("id"));
            b.setMysqlID(rs.getInt("mysqlID"));
            b.setRedisID(rs.getString("redisID"));
            b.setRequest(rs.getString("request"));
            b.setRequestDate(rs.getString("requestDate"));
            b.setTitle(rs.getString("title"));
            b.setDescription(rs.getString("description"));

            event.add(b);
        }
        return event;
    }

    public static final String SQL_REDIS_UPDATE_NEW_USER_EVENT_REQUEST_CONFIRM = """
            update allEvent
            set confirm = ?
            where id = ?
            """;

    public boolean updateRedisNewEventConfirm(String myUsername, int id) {
        int result = template.update(SQL_REDIS_UPDATE_NEW_USER_EVENT_REQUEST_CONFIRM,
                myUsername, id);
        System.out.printf("--- update new event to confirm: %d\n", result);
        return result > 0;
    }

}
