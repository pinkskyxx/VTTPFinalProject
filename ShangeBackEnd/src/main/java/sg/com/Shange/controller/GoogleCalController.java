package sg.com.Shange.controller;


import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import sg.com.Shange.models.CalendarEvent;
import sg.com.Shange.models.EventDetails;
import sg.com.Shange.models.FoodDetails;
import sg.com.Shange.models.UserProfile;
import sg.com.Shange.models.UserRegister;
import sg.com.Shange.Utils;
import java.util.List;

@Controller
public class GoogleCalController {

	private final static Log logger = LogFactory.getLog(GoogleCalController.class);
	private static final String APPLICATION_NAME = "";
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static com.google.api.services.calendar.Calendar client;
	private static com.google.api.services.calendar.Calendar client1;

	GoogleClientSecrets clientSecrets;
	GoogleAuthorizationCodeFlow flow;
	Credential credential;

	@Value("${google.client.client-id}")
	private String clientId;
	@Value("${google.client.client-secret}")
	private String clientSecret;
	@Value("${google.client.redirectUri}")
	private String redirectURI;

    @Value("${s3.image.location}")
    private String ImageLocation;

	private Set<Event> events = new HashSet<>();

	final DateTime date1 = new DateTime("2017-05-05T16:30:00.000+05:30");
	final DateTime date2 = new DateTime(new Date());
	final long dateDist = 545;

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	@RequestMapping(value = "/login/googleCheck", method = RequestMethod.GET)
	public String googleConnectionCheck(HttpServletRequest request, HttpSession sess) throws Exception {
		 UserProfile myProfile = (UserProfile) sess.getAttribute(sg.com.Shange.Utils.Sess_Profile);
		 clientId = myProfile.getClientId();
		 clientSecret = myProfile.getClientSecret();
		System.out.println("I am enterring " + clientId);
		System.out.println("I am enterring " + clientSecret);
		System.out.println();
		UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
		String type = registerName.getType();
		if(clientId=="" || clientId==null || clientSecret=="" || clientSecret==null)
		{
			if (registerName.getType().equals("organization"))
				return "homeSetupCalenderOrganisation";
			else
				return "homeSetupCalenderVolunteer";
		}
			

		return "redirect:/login/google";
	}
	@RequestMapping(value = "/login/google", method = RequestMethod.GET)
	public RedirectView googleConnectionStatus(HttpServletRequest request, HttpSession sess) throws Exception {
		 UserProfile myProfile = (UserProfile) sess.getAttribute(sg.com.Shange.Utils.Sess_Profile);
		 clientId = myProfile.getClientId();
		 clientSecret = myProfile.getClientSecret();
		System.out.println("I am enterring " + clientId);
		System.out.println("I am enterring " + clientSecret);
		System.out.println();
		return new RedirectView(authorize());
	}

	@RequestMapping(value = "/login/google", method = RequestMethod.GET, params = "code")
        public String oauth2Callback(@RequestParam(value = "code") String code, Model model, HttpSession sess ) {

		System.out.println("Code: " + code);
		com.google.api.services.calendar.model.Events eventList;
		String message;
		List<CalendarEvent> myEventList = new LinkedList();
		try {
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
			credential = flow.createAndStoreCredential(response, "userID");
			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME).build();
					client1 = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME).build();
					
			//get event
			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME).build();
			Events events = client.events();

			LocalDateTime date2a = LocalDateTime.now();
			LocalDateTime date3 = date2a.plusDays(dateDist);
			ZonedDateTime zdtEnd = date3.atZone(ZoneId.systemDefault());
			DateTime endTime = new DateTime(java.util.Date.from(zdtEnd.toInstant()));

			eventList = events.list("primary").setTimeMin(date1).setTimeMax(endTime).execute();
			message = eventList.getItems().toString();
			System.out.println("My:" + eventList.getItems());
			System.out.println();			
			
			for (Event event : eventList.getItems()) {
				// Access event properties
				String id = event.getId();
				String summary = event.getSummary();
				DateTime startDateTime = event.getStart().getDateTime();
				DateTime endDateTime = event.getEnd().getDateTime();
				String eventID = event.getId();
				String description = event.getDescription();

				if(startDateTime!=null && endDateTime!=null)
				{
					CalendarEvent formattedEvent = formatEvent(id,summary, description,startDateTime, endDateTime);
					myEventList.add(formattedEvent);
        			System.out.println("formattedEvent: "+formattedEvent);
				}
				System.out.println("---end formated--");
				String googlecalendar = (String) sess.getAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_login);
				System.out.println(googlecalendar);
				if (googlecalendar == "false") {
					sess.setAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_login, "true");
					model.addAttribute("sessionGoogleCalendar", "");
				}
			}
		} catch (Exception e) {
			logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.");
			message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.";
		}

		System.out.println();
		model.addAttribute("myCalenderEvent", myEventList);
		UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
		if (registerName.getType().equals("organization"))
			{
				model.addAttribute("regeventName", registerName.getFullName());
				model.addAttribute("regeventDate", LocalDate.now());
				return "homepageOrganisationCalendar";
			}
		else
		{
			model.addAttribute("regeventName", registerName.getFullName());
			model.addAttribute("regeventDate", LocalDate.now());
			return "homepageVolunteerCalendar";
		}
			
        
	}

	@RequestMapping(value = "/calendar/deleteEvent/{id}", method = RequestMethod.POST)
        public String calendarDeleteEvent(@PathVariable("id") String id, Model model, HttpSession sess ) {

		System.out.println("id: " + id);
		com.google.api.services.calendar.model.Events eventList;
		String message;
		List<CalendarEvent> myEventList = new LinkedList();
		try {
			client = client1;
			System.out.println();
			 System.out.println("delete: " + client.events().delete( "primary" ,id).execute());
			System.out.println();

			LocalDateTime date2a = LocalDateTime.now();
			LocalDateTime date3 = date2a.plusDays(dateDist);
			ZonedDateTime zdtEnd = date3.atZone(ZoneId.systemDefault());
			DateTime endTime = new DateTime(java.util.Date.from(zdtEnd.toInstant()));
			Events events = client.events();
			eventList = events.list("primary").setTimeMin(date1).setTimeMax(endTime).execute();
			message = eventList.getItems().toString();
			System.out.println("My:" + eventList.getItems());
			System.out.println();

			for (Event event : eventList.getItems()) {
				String myid = event.getId();
				String summary = event.getSummary();
				DateTime startDateTime = event.getStart().getDateTime();
				DateTime endDateTime = event.getEnd().getDateTime();
				String eventID = event.getId();
				String description = event.getDescription();

				System.out.println("---formated--");

				if(startDateTime!=null && endDateTime!=null)
				{
					CalendarEvent formattedEvent = formatEvent(myid,summary, description,startDateTime, endDateTime);
					myEventList.add(formattedEvent);
        			System.out.println("formattedEvent: "+formattedEvent);
				}
				System.out.println("---end formated--");
			}
		} catch (Exception e) {
			logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.");
			message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.";
		}
		System.out.println();
		model.addAttribute("myCalenderEvent", myEventList);

		UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
		if (registerName.getType().equals("organization"))
			{
				model.addAttribute("regeventName", registerName.getFullName());
				model.addAttribute("regeventDate", LocalDate.now());
				return "homepageOrganisationCalendar";
			}
		else
		{
			model.addAttribute("regeventName", registerName.getFullName());
			model.addAttribute("regeventDate", LocalDate.now());
			return "homepageVolunteerCalendar";
		}
	}


	
	@RequestMapping(value = "/calendar/createEvent", method = RequestMethod.GET)
        public String calendarCreateEvent(Model model, HttpSession sess) {
			System.out.println("Enter /calendar/createEvent");
		
		String message;
		// List<CalendarEvent> myEventList = new LinkedList();
		CalendarEvent googlecalendar = (CalendarEvent) sess.getAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_details);
			System.out.println(googlecalendar.getStartDate());
			System.out.println(googlecalendar.getEndDate());

		try {
						
						Event eventii = new Event()
						.setSummary(googlecalendar.getSummary())
						.setDescription(googlecalendar.getDescription())
						.setStart(new EventDateTime().setDateTime(new DateTime(googlecalendar.getStartDate())))
						.setEnd(new EventDateTime().setDateTime(new DateTime(googlecalendar.getEndDate())));
			
						Event createdEvent = client.events().insert("primary", eventii).execute();
						System.out.println("Event created: " + createdEvent.getHtmlLink());
			}
		 catch (Exception e) {
			logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.");
			message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.";
		}

		System.out.println();
		// FoodDetails foodDetails = (FoodDetails) sess.getAttribute(sg.com.Shange.Utils.Sess_EventFoodDetails);
		List<EventDetails> eventDetails = (List<EventDetails>)sess.getAttribute(Utils.Sess_EventDetails);
		// List<EventDetails> eventDetails = databaseService.getAllEvent();
        for (EventDetails iterable_element : eventDetails) {
            
            String img =  iterable_element.getStrImage();
            if(img!="" && img!=null)
            {
                if(!img.contains(ImageLocation))
                {
                    img = ImageLocation + img;
                    iterable_element.setStrImage(img);
                }
            }
            String filename =  iterable_element.getStrFile();
            if(filename!="" && filename!=null)
            {
                if(!filename.contains(ImageLocation)){
                    filename = ImageLocation + filename;
                    iterable_element.setStrFile(filename);
                }
            }
        }
        sess.setAttribute(Utils.Sess_EventDetails, eventDetails);

		if(eventDetails!=null)
		  model.addAttribute("myEventDetails", eventDetails);
		
        return "homepageOrganisation";
	}

	public Set<Event> getEvents() throws IOException {
		return this.events;
	}

	private String authorize() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if (flow == null) {
			Details web = new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					Collections.singleton(CalendarScopes.CALENDAR)).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
		System.out.println("cal authorizationUrl->" + authorizationUrl);
		return authorizationUrl.build();
	}

	private static CalendarEvent formatEvent(String id, String title, String description, DateTime start, DateTime end) {
        LocalDateTime startDateTime = convertDateTime(start);
        LocalDateTime endDateTime = convertDateTime(end);

		CalendarEvent calendarEvent = new CalendarEvent();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
		String startDate = startDateTime.format(formatterDate);
        String formattedStart = startDateTime.format(formatterTime);
        String formattedEnd = endDateTime.format(formatterTime);
		calendarEvent.setId(id);
		calendarEvent.setSummary(title);
		calendarEvent.setDescription(description);
		calendarEvent.setStartDate(startDate);
		calendarEvent.setStartTime(formattedStart);
		calendarEvent.setEndTime(formattedEnd);

				return calendarEvent;
    }

	private static String formatEventa(String title, DateTime start, DateTime end) {
        LocalDateTime startDateTime = convertDateTime(start);
        LocalDateTime endDateTime = convertDateTime(end);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String formattedStart = startDateTime.format(formatter);
        String formattedEnd = endDateTime.format(formatter);

        return "Event: " + title + "\n" +
                "Start Time: " + formattedStart + "\n" +
                "End Time: " + formattedEnd;
    }

    private static LocalDateTime convertDateTime(DateTime dateTime) {
        Instant instant = Instant.ofEpochMilli(dateTime.getValue());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
