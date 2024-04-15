package sg.com.Shange.repositories;


import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;

import sg.com.Shange.Utils;
import sg.com.Shange.models.EventDetails;
import sg.com.Shange.models.Meal;
import sg.com.Shange.models.OrganisationProfile;
import sg.com.Shange.models.VolunteerProfile;


@Repository
public class RedisRepository {

	@Autowired
	@Qualifier(Utils.REDIS_USER)
	private RedisTemplate<String, String> templateDB0;

	@Autowired
	@Qualifier(Utils.REDIS_NewUSER)
	private RedisTemplate<String, String> templateDB1;

	@Autowired
	@Qualifier(Utils.REDIS_Volunteer)
	private RedisTemplate<String, String> templateDB2;

	@Autowired
	@Qualifier(Utils.REDIS_Food)
	private RedisTemplate<String, Object> templateDB3;

	@Autowired
	@Qualifier(Utils.REDIS_EventDetails)
	private RedisTemplate<String, Object> templateDB4;

	@Autowired
	@Qualifier(Utils.REDIS_EventUser)
	private RedisTemplate<String, String> templateDB5;

	@Autowired
	@Qualifier(Utils.REDIS_VolunteerProfile)
	private RedisTemplate<String, Object> templateDB7;

	@Autowired
	@Qualifier(Utils.REDIS_OrganisationProfile)
	private RedisTemplate<String, Object> templateDB8;


	public void saveNewFood(String id, Meal userDetail) {
		templateDB3.opsForValue()
				.set(id, userDetail);
	}

	public void saveNewEventDetails(String id, EventDetails userDetail) {
		templateDB4.opsForValue()
				.set(id, userDetail);
	}

	public void saveNewEventUser(String id, String userDetail) {
		templateDB5.opsForValue()
				.set(id, userDetail);
	}

	public void saveVolunterProfile(String id, VolunteerProfile userDetail) {
		templateDB7.opsForValue()
				.set(id, userDetail);
	}

	public void saveOrganisationProfile(String id, OrganisationProfile userDetail) {
		templateDB8.opsForValue()
				.set(id, userDetail);
	}

	public int getNumberOfUser() {
		Set<String> opsValue = templateDB0.keys("*");
		return opsValue.size();
	}

	public int getNumberOfNewUser() {
		Set<String> opsValue = templateDB1.keys("*");
		return opsValue.size();
	}

	public int getNumberOfVolunnteer() {
		Set<String> opsValue = templateDB2.keys("*");
		return opsValue.size();
	}

	public int getNumberOfFood() {
		Set<String> opsValue = templateDB3.keys("*");
		return opsValue.size();
	}

	public int getNumberOfEventDetails() {
		Set<String> opsValue = templateDB4.keys("*");
		return opsValue.size();
	}

	public int getNumberOfEventUser() {
		Set<String> opsValue = templateDB5.keys("*");
		return opsValue.size();
	}

	public int getNumberOfVolunteerProfile() {
		Set<String> opsValue = templateDB7.keys("*");
		return opsValue.size();
	}

	public int getNumberOfOrganisationProfile() {
		Set<String> opsValue = templateDB8.keys("*");
		return opsValue.size();
	}

	public Set<String> getAllUsers() {
		Set<String> opsValue = templateDB0.keys("*");
		return opsValue;
	}

	public Set<String> getAllNewUsers() {
		Set<String> opsValue = templateDB1.keys("*");
		return opsValue;
	}

	public Set<String> getAllVolunnteer() {
		Set<String> opsValue = templateDB2.keys("*");
		return opsValue;
	}

	public Set<String> getAllFood() {
		Set<String> opsValue = templateDB3.keys("*");
		return opsValue;
	}

	public Set<String> getAllEventDetails() {
		Set<String> opsValue = templateDB4.keys("*");
		return opsValue;
	}

	public Set<String> getAllEventUser() {
		Set<String> opsValue = templateDB5.keys("*");
		return opsValue;
	}

	public Set<String> getAllVolunnterProfile() {
		Set<String> opsValue = templateDB7.keys("*");
		return opsValue;
	}

	public Set<String> getAllOrganisationProfile() {
		Set<String> opsValue = templateDB8.keys("*");
		return opsValue;
	}

	public String getUser(String id) {
		
		String value = "";
		if (templateDB0.opsForValue().get(id) == null)
			value = "null";
		else {
			value = templateDB0.opsForValue().get(id);
		}
		return value;
	}

	public String getNewUser(String id) {
		String value = "";
		if (templateDB1.opsForValue().get(id) == null) {
			value = "null";
		} else {
			value = templateDB1.opsForValue().get(id);
		}
		return value;
	}

	public String getVolunteer(String id) {
		String value = "";
		if (templateDB2.opsForValue().get(id) == null) {
			value = "null";
		} else {
			value = templateDB2.opsForValue().get(id);
		}
		return value;
	}

	public Meal getFood(String id) {
		Meal userlogin = new Meal();
		String value = "";
		if (templateDB3.opsForValue().get(id) == null) {
			value = "null";
		} else {
			userlogin = (Meal) templateDB3.opsForValue().get(id);
		}
		return userlogin;
	}

	public EventDetails getEventDetails(String id) {
		EventDetails userlogin = new EventDetails();
		String value = "";
		if (templateDB4.opsForValue().get(id) == null) {
			value = "null";
		} else {
			userlogin = (EventDetails) templateDB4.opsForValue().get(id);
		}
		return userlogin;
	}

	public String getEventUser(String id) {
		String value = "";
		if (templateDB5.opsForValue().get(id) == null) {
			value = "null";
		} else {
			value = templateDB5.opsForValue().get(id);
		}
		return value;
	}

	public VolunteerProfile getVolunteerProfileDetails(String id) {
		VolunteerProfile userlogin = new VolunteerProfile();
		String value = "";
		if (templateDB7.opsForValue().get(id) == null) {
			value = "null";
		} else {
			userlogin = (VolunteerProfile) templateDB7.opsForValue().get(id);
		}
		return userlogin;
	}

	public OrganisationProfile getOrganisationProfileDetails(String id) {
		OrganisationProfile userlogin = new OrganisationProfile();
		String value = "";
		if (templateDB8.opsForValue().get(id) == null) {
			value = "null";
		} else {
			userlogin = (OrganisationProfile) templateDB8.opsForValue().get(id);
		}
		return userlogin;
	}

	public Boolean checkEventDetails(String id) {
		Boolean value = true;
		System.out.println("Check details");
		System.out.println("Check: " + templateDB4.opsForValue().get(id));
		if (templateDB4.opsForValue().get(id) == null) {
			value = false;
		} else {
		}
		return value;
	}

	public void deleteUser(String id) {
		templateDB0.delete(id);
	}

	public void deleteNewUser(String id) {
		templateDB1.delete(id);
	}

	public void deleteVolunteer(String id) {
		templateDB2.delete(id);
	}

	public void deleteFood(String id) {
		templateDB3.delete(id);
	}

	public void deleteEventDetails(String id) {
		templateDB4.delete(id);
	}

	public void deleteEventUser(String id) {
		templateDB5.delete(id);
	}

	public void deleteVolunteerProfile(String id) {
		templateDB7.delete(id);
	}

	public void deleteOrganisationProfile(String id) {
		templateDB8.delete(id);
	}
}
