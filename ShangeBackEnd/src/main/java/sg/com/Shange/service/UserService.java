package sg.com.Shange.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import sg.com.Shange.models.EventDetails;
import sg.com.Shange.models.MapRequest;
import sg.com.Shange.models.MapRequestModify;
import sg.com.Shange.models.ProfileJointUser;
import sg.com.Shange.models.UserLogin;
import sg.com.Shange.models.UserProfile;
import sg.com.Shange.models.UserRegister;
import sg.com.Shange.models.allEvent;
import sg.com.Shange.repositories.ImageRepository;
import sg.com.Shange.repositories.RedisRepository;
import sg.com.Shange.repositories.UserRepository;
import sg.com.Shange.security.SpringSecurityConfiguration;

@Service
public class UserService {

	@Autowired
	private PlatformTransactionManager txMgr;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private SpringSecurityConfiguration springSecurityConfiguration;


	public boolean createNewUserManualTx(UserRegister userInfo) {
		System.out.printf(">>>> using manual user insert transaction \n");
		TransactionStatus tx = txMgr.getTransaction(null);
	
		try {
			boolean result = userRepo.insertNewUser(userInfo) && userRepo.insertNewUserLogin(userInfo);
			UserLogin a = new UserLogin();
			a.setUserName(userInfo.getUserName());
			a.setUserPassword(userInfo.getPassword());
			a.setUserType(userInfo.getType());
			this.Add_user_to_system(a);
			txMgr.commit(tx);
			return result;
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back user insert transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
		return false;
	}

	public boolean Check_UserExist(UserRegister userInfo) {
		// public Optional<User> userExists(String email)

		System.out.println("Checking User Exist");
		String chkUser = userInfo.getUserName();
		boolean user = userRepo.CheckUserExists(chkUser);
		System.out.println(user);
		return user;
	}

	public UserRegister Get_UserInfo(String username) {
		System.out.println("get user info");
		UserRegister chkUser = userRepo.GetUserRegisteredDetails(username);
		System.out.println(chkUser);
		return chkUser;
	}

	public boolean Check_ProfileExist(String id) {
		System.out.println("Checking Profile Exist");
		boolean chkProfile = userRepo.CheckProfileExists(id);
		System.out.println(chkProfile);
		return chkProfile;
	}

	public UserProfile Get_UserProfileInfo(String username) {
		System.out.println("get profile info");
		UserProfile chkUser = userRepo.GetUserProfile(username);
		System.out.println(chkUser);
		return chkUser;
	}

	public List<UserProfile> Get_AllProfileInfo() {
		System.out.println("get All profile info");
		List<UserProfile>  chkUser = userRepo.GetAllProfile();
		return chkUser;
	}

	public List<allEvent> Get_AllNewEventVolunteerToConfirm(String username) {
		System.out.println("get All volunteer to confirm info");
		List<allEvent>  chkUser = userRepo.getNewEventRequestRedisExistsConfirm(username);
		return chkUser;
	}

	public List<UserProfile> Get_AllProfileInfoType(String type) {
		System.out.println("get All profile info : " + type);
		List<UserProfile>  chkUser = userRepo.GetAllProfileType(type);
		return chkUser;
	}

	public List<ProfileJointUser> Get_AllProfileInfoJointType(String type) {
		System.out.println("get All profile Joint info : " + type);
		List<ProfileJointUser>  chkUser = userRepo.GetAllProfileJoint(type);
		return chkUser;
	}

	public boolean createNewProfileManualTx(UserProfile userInfo) {
		System.out.printf(">>>> using manual profile insert transaction \n");
		TransactionStatus tx = txMgr.getTransaction(null);
		
		try {
			boolean result = userRepo.insertProfile(userInfo);
			txMgr.commit(tx);
			return result;
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back manual profile insert transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
		return false;
	}

	public boolean updateProfileManualTx(UserProfile userInfo) {
		System.out.printf(">>>> using manual profile update transaction \n");
		TransactionStatus tx = txMgr.getTransaction(null);
	
		try {
			boolean result = userRepo.UpdateProfile(userInfo);
			txMgr.commit(tx);
			return result;
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back manual profile insert transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
		return false;
	}
	public boolean createNewMapEventRequestManualTx(MapRequest userInfo) {
		System.out.printf(">>>> using manual map event request insert transaction \n");
		TransactionStatus tx = txMgr.getTransaction(null);
		
		try {
			boolean result = userRepo.insertNewMapEventRequest(userInfo);
			txMgr.commit(tx);
			return result;
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back manual map event request insert transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
		return false;
	}

	public boolean InitUserToMain() {

		List<UserLogin> allNewUserDetails = userRepo.GetAllLoginUser();
		System.out.println("Number of users: " + allNewUserDetails.size());
		if (allNewUserDetails.size() > 0) {
			for (UserLogin indLogin : allNewUserDetails) {
				System.out.println(indLogin);
				String user = indLogin.getUserName();
				String pw = indLogin.getUserPassword();
				String type = indLogin.getUserType();

				if (type.equals("volunteer"))
					springSecurityConfiguration.addUser(user, pw, "USER");
				else {
					springSecurityConfiguration.addUser(user, pw, "ADMIN");
					System.out.println("Admin: " + indLogin);
				}
			}
			return true;
		}
		return false;
	}

	public boolean Add_user_to_system(UserLogin indLogin) {

		System.out.println("Add user: " + indLogin);

		String user = indLogin.getUserName();
		String pw = indLogin.getUserPassword();
		String type = indLogin.getUserType();

		if (type.equals("volunteer"))
			springSecurityConfiguration.addUser(user, pw, "USER");
		else {
			springSecurityConfiguration.addUser(user, pw, "ADMIN");
			System.out.println("Admin: " + indLogin);
		}

		return true;

	}

	// map event modify
	public List<MapRequestModify> findByEventDateBetween(Date fromDate, Date toDate) {

		System.out.println("Checking User Exist");
		List<MapRequestModify> chkUserdata = userRepo.allMapEventModify(fromDate, toDate);

		System.out.println(chkUserdata);
		return chkUserdata;
	}

	public List<MapRequestModify> GetAllMapEventAll() {

		System.out.println("Get All Map Event");
		List<MapRequestModify> alldata = userRepo.GetallMapEventModify();

		return alldata;
	}
	public int getEventSqlId(String myid, String title) {
		System.out.println("Get sqlID");
		int replyID = userRepo.getMapEventId(myid, title);

		System.out.println(replyID);
		return replyID;
	}

	public String getEventSqlReplyByID(int id) {
		System.out.println("Checking User Exist");
		String reply = userRepo.getMapEventReplyById(id);
		System.out.println(reply);
		return reply;
	}

	public boolean updateEventSqlReplyByID(int id, String reply) {
		System.out.println("Update reply by id");
		boolean result = userRepo.updateMapEventModifyReplyById(id,reply);
		System.out.println(reply);
		return result;
	}

	public boolean updateEventRedisRequestToConfirm(allEvent aEvent) {
		System.out.println("Update volunteer request to confirm by id");
		boolean result = userRepo.updateRedisNewEventConfirm(aEvent.getUserName(),aEvent.getId());
		System.out.println(result);
		return result;
	}

	public boolean updateEventSqlReplyByIDFullChecking(int id, String reply) {
		System.out.println("Update reply by id on full checking");
		boolean myReply=false;
		String retreply = getEventSqlReplyByID(id);
		if(retreply==null)
			retreply="";
		if(retreply.contains(reply+",")){
			myReply = false;
		}else{
			retreply+= reply +", ";
			System.out.println("Now do insert: " + retreply);
			boolean result = userRepo.updateMapEventModifyReplyById(id,retreply);
			myReply = true;
		}

		System.out.println("The reply result  is: "+retreply + " " + myReply);
		return myReply;
	}

	public boolean deleteMapEventModifyManualTx(int id) {
		System.out.printf(">>>> using manual map event request delete transaction \n");
		TransactionStatus tx = txMgr.getTransaction(null);
		
		try {
			boolean result = userRepo.deleteMapEventModifyById(id);
			txMgr.commit(tx);
			return result;
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back manual map event request delete transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
		return false;
	}

	public boolean updateMapEventModifyManualTx(int id, MapRequestModify userInfo) {
		System.out.printf(">>>> using manual map event request update transaction \n");
		TransactionStatus tx = txMgr.getTransaction(null);
	
		try {
			boolean result = userRepo.updateMapEventModifyById(id, userInfo);
			txMgr.commit(tx);
			return result;
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back manual map event request update transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
		return false;
	}

		public String postPicture(MultipartFile picture) throws IOException {
		String imageName = UUID.randomUUID().toString();
		imageRepository.putObject(picture, imageName);
		return imageName;
		
	}

	public boolean insertNewEventSQLManualTx(allEvent Info) {
		System.out.printf(">>>> using manual insert SQL Event transaction \n");
		TransactionStatus tx = txMgr.getTransaction(null);
		
		try {
			System.out.println(Info);
			boolean result = userRepo.insertSQLNewUserEvent(Info);
			txMgr.commit(tx);
			return result;
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back insert SQL Event transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
		return false;
	}

	public boolean insertNewEventRedisManualTx(allEvent Info) {
		System.out.printf(">>>> using manual insert Redis Event transaction \n");
		TransactionStatus tx = txMgr.getTransaction(null);
	
		try {
			boolean result = userRepo.insertRdisNewUserEvent(Info);
			txMgr.commit(tx);
			return result;
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back insert Redis Event transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
		return false;
	}

	public boolean insertNewEventRedisRequestManualTx(allEvent Info) {
		System.out.printf(">>>> using manual insert Redis Event Request transaction \n");
		if(Check_UserNewEventRedisRequestExist(Info))
			return false;
		TransactionStatus tx = txMgr.getTransaction(null);
		
		try {
			boolean result = userRepo.insertRdisNewUserEventRequest(Info);
			txMgr.commit(tx);
			return result;
		} catch (Exception ex) {
			System.out.printf(">>>> rolling back insert Redis Event Request transaction\n");
			ex.printStackTrace();
			txMgr.rollback(tx);
		}
		return false;
	}

	public boolean Check_UserNewEventRedisRequestExist(allEvent userInfo) {

		String chkUser = userInfo.getRequest();
		String RegisID = userInfo.getRedisID();
		System.out.println("Checking User Exist " + chkUser + " " + RegisID);
		boolean user = userRepo.CheckNewEventRequestRedisExists(chkUser, RegisID);
		System.out.println(user);
		return user;
	}

	public List<allEvent> Get_UserNewEventOwnRequest(String username) {
		System.out.println("Checking User Exist");
		List<allEvent> ae = userRepo.GetAllNewEventOwnRequest(username);
		System.out.println(ae);
		return ae;
	}
}
