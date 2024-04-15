package sg.com.Shange.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import sg.com.Shange.models.EventDetails;
import sg.com.Shange.models.Meal;
import sg.com.Shange.models.OrganisationProfile;
import sg.com.Shange.models.VolunteerProfile;
import sg.com.Shange.repositories.RedisRepository;
import sg.com.Shange.security.SpringSecurityConfiguration;

@Service
public class DatabaseService {

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private SpringSecurityConfiguration springSecurityConfiguration;


	public int getNumberOfNewUser() {
		return redisRepository.getNumberOfNewUser();
	}

	private static String getValue(String part) {
		String[] keyValue = part.split("=");
		return keyValue[1];
	}

	public Boolean saveAllFood(List<Meal> myMeals) {
		for (Meal string : myMeals) {
			String idmail = string.getIdMeal();
			redisRepository.deleteFood(idmail);
			redisRepository.saveNewFood(idmail, string);
		}
		return true;
	}

	public Meal getFood(String myMealsId) {
		Meal meal = new Meal();
		meal = redisRepository.getFood(myMealsId);
		return meal;
	}

	public Boolean checkEventDetails(String id) {
		Boolean value = false;
		System.out.println("Event id: " + id);
		if (redisRepository.checkEventDetails(id) == true) {
			System.out.println("true");
			value = true;
		}
		return value;
	}

	public Boolean saveEventDetails(String id, EventDetails eventDetails) {
		Boolean value = true;
		redisRepository.deleteEventDetails(id);
		redisRepository.saveNewEventDetails(id, eventDetails);
		return value;
	}

	public Boolean saveVolunteerProfile(VolunteerProfile profile) {
		String userName = profile.getUserName();
		redisRepository.deleteVolunteerProfile(userName);
		redisRepository.saveVolunterProfile(userName, profile);

		return true;
	}

	public VolunteerProfile getVolunteerProfile(String id) {
		return redisRepository.getVolunteerProfileDetails(id);
	}

public Boolean saveOrganisationProfile(OrganisationProfile profile) {
		String userName = profile.getUserName();
		redisRepository.deleteOrganisationProfile(userName);
		redisRepository.saveOrganisationProfile(userName, profile);
		return true;
	}

	public OrganisationProfile getOrganisationProfile(String id) {
		return redisRepository.getOrganisationProfileDetails(id);
	}

	public List<EventDetails> getAllEvent() {
		List<EventDetails> allEventDetails = new ArrayList<>();
		int allEventcnt = redisRepository.getNumberOfEventDetails();
		Set<String> allEventlist = redisRepository.getAllEventDetails();
		for (String string : allEventlist) {
			EventDetails eventDetails = redisRepository.getEventDetails(string);
			if (eventDetails != null) {
				allEventDetails.add(eventDetails);
			}
		}
		return allEventDetails;
	}
	public Boolean deleteCurrentEvent(String id) {
		redisRepository.deleteEventDetails(id);
		return true;
	}

}