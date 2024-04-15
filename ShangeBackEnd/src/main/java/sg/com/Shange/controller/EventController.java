package sg.com.Shange.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;
import sg.com.Shange.Utils;
import sg.com.Shange.models.CalendarEvent;
import sg.com.Shange.models.EventDetails;
import sg.com.Shange.models.FoodDetails;
import sg.com.Shange.models.MapRequestModify;
import sg.com.Shange.models.Meal;
import sg.com.Shange.models.MealCategories;
import sg.com.Shange.models.UserRegister;
import sg.com.Shange.models.allEvent;
import sg.com.Shange.service.DatabaseService;
import sg.com.Shange.service.UserService;

@Controller
@RequestMapping(path = "/event")
public class EventController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${spring.redirectUri}")
    private String redirectUri;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private UserService userservice;

    @Value("${s3.image.location}")
    private String ImageLocation;

    @PostMapping("/createEvent")
    public ModelAndView postCreateEvent(
            @RequestParam(name = "eventType", required = false) String eventType,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(name = "typePhysical", defaultValue = "false") String typePhysical,
            @RequestParam(name = "typeOnline", defaultValue = "false") String typeOnline,
            @RequestParam(name = "locationNorth", required = false) String locationNorth,
            @RequestParam(name = "locationSouth", required = false) String locationSouth,
            @RequestParam(name = "locationEast", required = false) String locationEast,
            @RequestParam(name = "locationWest", required = false) String locationWest,
            @RequestParam(name = "locationCentral", required = false) String locationCentral,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("file") MultipartFile fileFile,
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("fromTime") String fromTime,
            @RequestParam("toTime") String toTime,
            HttpSession sess) {

        // ModelAndView mav = new ModelAndView("homepageOrganisation");
        ModelAndView mav = new ModelAndView("redirect:/calendar/createEvent");

        String googlecalendar = (String) sess.getAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_login);
        System.out.println(googlecalendar);
        if (googlecalendar == "false") {
            // sess.setAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_login, "true");
            // model.addAttribute("sessionGoogleCalendar", "");
            mav = new ModelAndView("homepageOrganisation");
        }

        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        if (registerName == null) {
            mav.addObject("signUpVolunteerUrl", redirectUri + "/SignUpVolunteer");
            mav.addObject("signUpOrganisationUrl", redirectUri + "/SignUpOrganisation");
            mav = new ModelAndView("login");
            return mav;
        }
        FoodDetails foodDetails = (FoodDetails) sess.getAttribute(sg.com.Shange.Utils.Sess_EventFoodDetails);

        // TODO: process POST request

        if ("One-time Event".equals(eventType)) {
            System.out.println("One-time Event selected");

        } else if ("Recurring Event".equals(eventType)) {
            System.out.println("Recurring Event selected");

        } else {
            System.out.println("No event type selected");
            // Handle the case when no event type is selected
        }
        System.out.println(title);
        System.out.println(description);

        System.out.println("Type Physical: " + typePhysical);
        System.out.println("Type Online: " + typeOnline);

        System.out.println("Type locationNorth: " + locationNorth);
        System.out.println("Type locationSouth: " + locationSouth);
        System.out.println("Type locationEast: " + locationEast);
        System.out.println("Type locationWest: " + locationWest);
        System.out.println("Type locationCentral: " + locationCentral);
        System.out.println("From " + fromDate);
        System.out.println("to " + toDate);
        System.out.println("From " + fromTime);
        System.out.println("to " + toTime);

        // if (locations != null) {
        // for (String location : locations) {
        // System.out.println("Selected location: " + location);
        // }
        // }
        // Validate and process the files
        String imageFileName = "";
        // if (!imageFile.isEmpty()) {
        // imageFileName = saveFile(imageFile);
        // }
        String fileFileName = "";
        // if (!fileFile.isEmpty()) {
        // fileFileName = saveFile(fileFile);
        // }
        try {
            if (!imageFile.isEmpty()) {
                imageFileName = userservice.postPicture(imageFile);
                System.out.println("filename: " + imageFileName);
                // myProfile.setImage(filename);
                // model.addAttribute("StrImageDisplay", ImageLocation + filename);

            } else {
                // if (!imageName.isEmpty()) {
                // model.addAttribute("StrImageDisplay", ImageLocation + imageName);
                // }
            }
        } catch (Exception e) {
        }
        try {
            if (!fileFile.isEmpty()) {
                fileFileName = userservice.postPicture(fileFile);
                System.out.println("filename: " + fileFileName);
                // myProfile.setImage(filename);
                // model.addAttribute("StrImageDisplay", ImageLocation + filename);

            } else {
                // if (!imageName.isEmpty()) {
                // model.addAttribute("StrImageDisplay", ImageLocation + imageName);
                // }
            }
        } catch (Exception e) {
        }
        System.out.println("image file: " + imageFile);
        System.out.println("file file: " + fileFile);

        EventDetails eventDetails = new EventDetails();
        String myUserName = registerName.getUserName();
        eventDetails.setStrUsername(myUserName);
        eventDetails.setStrEventtype(eventType);
        eventDetails.setStrTitle(title);
        eventDetails.setStrDescription(description);
        eventDetails.setStrPhysical(typePhysical);
        eventDetails.setStrOnline(typeOnline);
        eventDetails.setStrNorth(locationNorth);
        eventDetails.setStrSouth(locationSouth);
        eventDetails.setStrEast(locationEast);
        eventDetails.setStrWest(locationWest);
        eventDetails.setStrCentral(locationCentral);
        if (foodDetails != null) {
            eventDetails.setStrSampleFood(foodDetails.getStrSampleFood());
            eventDetails.setStrSampleFoodSite1(foodDetails.getStrSampleFoodSite1());
            eventDetails.setStrSampleFoodSite2(foodDetails.getStrSampleFoodSite2());
            sess.setAttribute(Utils.Sess_EventFoodDetails, null);
        } else {
            eventDetails.setStrSampleFood("");
            eventDetails.setStrSampleFoodSite1("");
            eventDetails.setStrSampleFoodSite2("");
        }

        eventDetails.setStrQuiz("");
        // eventDetails.setStrImage(imageFile);
        // eventDetails.setStrFile(fileFile);
        eventDetails.setStrImage(imageFileName);
        eventDetails.setStrFile(fileFileName);
        eventDetails.setStrEventFrom(fromDate);
        eventDetails.setStrEventTo(toDate);
        eventDetails.setStrTimeFrom(fromTime);
        eventDetails.setStrTimeTo(toTime);
        eventDetails.setStrVolunteer("");
        // LocalDate mydatetime = LocalDate.now();
        for (int i = 1; i < 100; i++) {

            // String setKey = generateKey(myUserName, getCurrentDate(),
            // getTwoDigitNumber(i));
            String setKey = generateKey(myUserName, toDate, getTwoDigitNumber(i));
            if (databaseService.checkEventDetails(setKey) == false) {
                eventDetails.setStrEventId(setKey);
                databaseService.saveEventDetails(setKey, eventDetails);
                i += 100;
            }

        }
        List<EventDetails> myEventDetails = new ArrayList<>();
        myEventDetails.add(eventDetails);
        sess.setAttribute(Utils.Sess_EventDetails, myEventDetails);
        List<EventDetails> myListEvents = new ArrayList<>();
        myListEvents.add(eventDetails);
        mav.addObject("myEventDetails", myEventDetails);
        System.out.println("Event deatils: " + myEventDetails);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        allEvent allevent = new allEvent();
        allevent.setUserName(myUserName);
        allevent.setRedisID(eventDetails.getStrEventId());
        allevent.setTitle(title);
        allevent.setDescription(description);
        allevent.setEventDate(fromDate);
        allevent.setRequestDate(formattedDate);
        System.out.println("result insert Redis: " + userservice.insertNewEventRedisManualTx(allevent));
        // RedirectAttributes redirectAttributes;
        // redirectAttributes.addFlashAttribute("someAttribute", "someValue");
        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setDescription(description);
        calendarEvent.setSummary(title);
        calendarEvent.setStartDate(convertTOcalendarDate(fromDate, fromTime));
        calendarEvent.setEndDate(convertTOcalendarDate(toDate, toTime));

        sess.setAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_details, calendarEvent);
        return mav;
    }

    private static String convertTOcalendarDate(String fromDateStr, String fromTimeStr) {

        LocalDate fromDate = LocalDate.parse(fromDateStr);

        DateTimeFormatter timeParserFormatter = DateTimeFormatter.ofPattern("hmma");

        LocalTime fromTime = LocalTime.parse(fromTimeStr.toUpperCase(), timeParserFormatter);
        LocalDateTime startDateTime = LocalDateTime.of(fromDate, fromTime);

        ZoneId zoneId = ZoneId.of("Asia/Singapore");
        ZonedDateTime zonedStartDateTime = ZonedDateTime.of(startDateTime, zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        String formattedStart = zonedStartDateTime.format(formatter);

        System.out.println("Start: " + formattedStart);

        return formattedStart;
    }

    private static String generateKey(String username, String formattedDate, String twoDigitNumber) {
        return username + "_" + formattedDate + "_" + twoDigitNumber;
    }

    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    private static String getTwoDigitNumber(int number) {
        // Ensure number is two digits
        return String.format("%02d", number);
    }

    private String saveFile(MultipartFile file) {
        String fileName = "";
        try {
            
            byte[] bytes = file.getBytes();

            fileName = file.getOriginalFilename();

            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            java.nio.file.Files.createDirectories(uploadPath);

            Path path = Paths.get(uploadDir + File.separator + fileName);

            java.nio.file.Files.write(path, bytes);

            System.out.println("File saved: " + path.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;

    }

    @GetMapping("/SearchEventOrganisation")
    public ModelAndView postSearchEventOrganisation(Model model, HttpSession sess) {

        System.out.println("SearchEventOrganisation");
        ModelAndView mav = new ModelAndView("homepageOrganisation");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        if (registerName != null) {
            mav.addObject("regeventName", registerName.getFullName());
            mav.addObject("regeventDate", LocalDate.now());
            // mav.addObject("regeventuserName", registerName.getUserName());
        }
        List<EventDetails> eventDetails = databaseService.getAllEvent();
        for (EventDetails iterable_element : eventDetails) {

            String img = iterable_element.getStrImage();
            if (img != "" && img != null) {
                if (!img.contains(ImageLocation)) {
                    img = ImageLocation + img;
                    iterable_element.setStrImage(img);
                }
            }
            String filename = iterable_element.getStrFile();
            if (filename != "" && filename != null) {
                if (!filename.contains(ImageLocation)) {
                    filename = ImageLocation + filename;
                    iterable_element.setStrFile(filename);
                }
            }
        }
        sess.setAttribute(Utils.Sess_EventDetails, eventDetails);
        mav.addObject("myEventDetails", eventDetails);
        // if (eventDetails != null) {

        // }

        return mav;
    }

    @GetMapping("/SearchEventVolunteer")
    public ModelAndView postSearchEventVolunteer(Model model, HttpSession sess) {

        System.out.println("SearchEventVolunteer");
        ModelAndView mav = new ModelAndView("homepageVolunteer");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        if (registerName != null) {
            mav.addObject("regeventName", registerName.getFullName());
            mav.addObject("regeventDate", LocalDate.now());
        }

        List<EventDetails> eventDetails = databaseService.getAllEvent();
        for (EventDetails iterable_element : eventDetails) {

            String img = iterable_element.getStrImage();
            if (img != "" && img != null) {
                if (!img.contains(ImageLocation)) {
                    img = ImageLocation + img;
                    iterable_element.setStrImage(img);
                }
            }
            String filename = iterable_element.getStrFile();
            if (filename != "" && filename != null) {
                if (!filename.contains(ImageLocation)) {
                    filename = ImageLocation + filename;
                    iterable_element.setStrFile(filename);
                }
            }
        }
        sess.setAttribute(Utils.Sess_EventDetails, eventDetails);
        if (eventDetails != null) {
            mav.addObject("myEventDetails", eventDetails);
        }

        return mav;
    }

    @PostMapping("/getToSaveRecipes")
    public ModelAndView getRecipes1(Model model, HttpSession sess) throws IOException {
        ModelAndView mav = new ModelAndView("homepageOrganisationFoodSearch");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);

        mav.addObject("dateOfFoodEvent", LocalDate.now());
        mav.addObject("regeventName", registerName.getFullName());

        String url1 = "https://www.themealdb.com/api/json/v1/1/categories.php";
        String url2 = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
        String payload;
        JsonArray meals;
        String url = UriComponentsBuilder
                .fromUriString(url2)
                .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
                .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;
        System.out.println("Part1");
        try {

            resp = template.exchange(req, String.class);

        } catch (Exception ex) {
            ex.printStackTrace();
            // return new LinkedList<>();
            return mav;
        }
        System.out.println("Now get body");
        payload = resp.getBody();
        String fname = "mypayload.txt";
        try (Writer fileWriter5 = new FileWriter(fname, false)) {
            fileWriter5.append(payload);
        }
        JsonReader reader = Json.createReader(new StringReader(payload));
        System.out.println("finish reader");
        JsonObject result = reader.readObject();
        System.out.println("finish result");
        meals = result.getJsonArray("meals");
       

        System.out.println("Now doing list");
        List<Meal> mymeals = new ArrayList<>();
        try {
            mymeals = meals.stream()
                    .map(j -> j.asJsonObject())
                    .map(o -> {
                        String idMeal = o.getString("idMeal", "Anonymous");
                        String strMeal = o.getString("strMeal", "");
                        String strDrinkAlternate = o.getString("strDrinkAlternate", "No description");
                        String strCategory = o.getString("strCategory", "");
                        String strArea = o.getString("strArea", "");
                        String strInstructions = o.getString("strInstructions", "");
                        String strMealThumb = o.getString("strMealThumb", "");
                        String strTags = o.getString("strTags", "");

                        String strYoutube = o.getString("strYoutube",
                                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/310px-Placeholder_view_vector.svg.png");
                        String strIngredient1 = o.getString("strIngredient1", "");
                        String strIngredient2 = o.getString("strIngredient2", "");
                        String strIngredient3 = o.getString("strIngredient3", "");
                        String strIngredient4 = o.getString("strIngredient4", "");
                        String strIngredient5 = o.getString("strIngredient5", "");
                        String strIngredient6 = o.getString("strIngredient6", "");
                        String strIngredient7 = o.getString("strIngredient7", "");
                        String strIngredient8 = o.getString("strIngredient8", "");
                        String strIngredient9 = o.getString("strIngredient9", "");
                        String strIngredient10 = o.getString("strIngredient10", "");
                        String strIngredient11 = o.getString("strIngredient11", "");
                        String strIngredient12 = o.getString("strIngredient12", "");
                        String strIngredient13 = o.getString("strIngredient13", "");
                        String strIngredient14 = o.getString("strIngredient14", "");
                        String strIngredient15 = o.getString("strIngredient15", "");
                        String strIngredient16 = o.getString("strIngredient16", "");
                        String strIngredient17 = o.getString("strIngredient17", "");
                        String strIngredient18 = o.getString("strIngredient18", "");
                        String strIngredient19 = o.getString("strIngredient19", "");
                        String strIngredient20 = o.getString("strIngredient20", "");
                        String strMeasure1 = o.getString("strMeasure1", "");
                        String strMeasure2 = o.getString("strMeasure2", "");
                        String strMeasure3 = o.getString("strMeasure3", "");
                        String strMeasure4 = o.getString("strMeasure4", "");
                        String strMeasure5 = o.getString("strMeasure5", "");
                        String strMeasure6 = o.getString("strMeasure6", "");
                        String strMeasure7 = o.getString("strMeasure7", "");
                        String strMeasure8 = o.getString("strMeasure8", "");
                        String strMeasure9 = o.getString("strMeasure9", "");
                        String strMeasure10 = o.getString("strMeasure10", "");
                        String strMeasure11 = o.getString("strMeasure11", "");
                        String strMeasure12 = o.getString("strMeasure12", "");
                        String strMeasure13 = o.getString("strMeasure13", "");
                        String strMeasure14 = o.getString("strMeasure14", "");
                        String strMeasure15 = o.getString("strMeasure15", "");
                        String strMeasure16 = o.getString("strMeasure16", "");
                        String strMeasure17 = o.getString("strMeasure17", "");
                        String strMeasure18 = o.getString("strMeasure18", "");
                        String strMeasure19 = o.getString("strMeasure19", "");
                        String strMeasure20 = o.getString("strMeasure20", "");
                        String strSource = o.getString("strSource", "");
                        String strImageSource = o.getString("strImageSource", "");
                        String strCreativeCommonsConfirmed = o.getString("strCreativeCommonsConfirmed", "");
                        String dateModified = o.getString("dateModified", "");

                        return new Meal(idMeal, strMeal, strDrinkAlternate, strCategory, strArea, strInstructions,
                                strMealThumb, strTags, strYoutube,
                                strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
                                strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
                                strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
                                strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20,
                                strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
                                strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
                                strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
                                strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20,
                                strSource, strImageSource, strCreativeCommonsConfirmed, dateModified);
                    })
                    .toList();
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
        }
        System.out.println("New return");
        mav.addObject("meal", mymeals);

        databaseService.saveAllFood(mymeals);


        return mav;
    }

    @PostMapping("/updateSaveRecipes")
    public ModelAndView getupdateSaveRecipes(Model model, HttpSession sess,
            @RequestParam(value = "selectedidMeal", required = false) List<String> selectedMeals,
            @RequestParam("dateOfFoodEvent") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfFoodEvent)
            throws IOException {
        ModelAndView mav = new ModelAndView("createNewEvent");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        LocalDate mydate = (LocalDate) model.getAttribute("dateOfFoodEvent");

        String foodId = "";

        int hrs = 0;
        FoodDetails foodDetails = new FoodDetails();
        System.out.println(selectedMeals);
        for (String id : selectedMeals) {
            foodId += id + ",";
            Meal meal = databaseService.getFood(id);
            System.out.println("id: " + id + " " + meal);
            String url1 = meal.getStrMealThumb();
            if (url1 != null) {
                if (url1.contains("http")) {
                    if (hrs == 1) {
                        foodDetails.setStrSampleFoodSite2(url1);

                        hrs++;
                    }
                    if (hrs == 0) {
                        foodDetails.setStrSampleFoodSite1(url1);
                        hrs++;
                    }
                }
            }
        }
        
        if (hrs > 0) {
            foodDetails.setStrSampleFood(foodId);
            mav.addObject("regeventFoodName", foodId);
            mav.addObject("strSampleFoodSite1", foodDetails.getStrSampleFoodSite1());
            mav.addObject("strSampleFoodSite2", foodDetails.getStrSampleFoodSite2());

            sess.setAttribute(Utils.Sess_EventFoodDetails, foodDetails);
        }
      
        mav.addObject("dateOfFoodEvent", LocalDate.now());
        mav.addObject("regeventName", registerName.getFullName());

        return mav;
    }

    @PostMapping("/deleteEvent")
    public ModelAndView deleteEvent(
            @RequestParam(value = "myselecteddeleteID", required = false) String myselecteddeleteID,
            @RequestParam(value = "myselectedUserID", required = false) String myselectedUserID,
            HttpSession sess) {
        ModelAndView mav = new ModelAndView("homepageOrganisation");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        System.out.println("delete: " + myselecteddeleteID + " userid:" + myselectedUserID + " regid: "
                + registerName.getUserName());

        if (registerName.getUserName().equals(myselectedUserID))
            databaseService.deleteCurrentEvent(myselecteddeleteID);
        else
            System.out.println("Different ID, cannot delete");
        List<EventDetails> eventDetails = databaseService.getAllEvent();
        for (EventDetails iterable_element : eventDetails) {

            String img = iterable_element.getStrImage();
            if (img != "" && img != null) {
                if (!img.contains(ImageLocation)) {
                    img = ImageLocation + img;
                    iterable_element.setStrImage(img);
                }
            }
            String filename = iterable_element.getStrFile();
            if (filename != "" && filename != null) {
                if (!filename.contains(ImageLocation)) {
                    filename = ImageLocation + filename;
                    iterable_element.setStrFile(filename);
                }
            }
        }
        sess.setAttribute(Utils.Sess_EventDetails, eventDetails);

        if (eventDetails != null) {
            mav.addObject("myEventDetails", eventDetails);
        }
        
        if (registerName != null) {
            mav.addObject("regeventName", registerName.getFullName());
            mav.addObject("regeventDate", LocalDate.now());
        }
        return mav;
    }

    @PostMapping("/insertRequestEvent")
    public ModelAndView insertRequestEvent(@RequestParam("eventall") String eventall, HttpSession sess) {

        ModelAndView mav = new ModelAndView("homepageVolunteer");
        System.out.println("The eventid= " + eventall);
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        List<EventDetails> ed = new ArrayList();
        ed = (List<EventDetails>) sess.getAttribute(sg.com.Shange.Utils.Sess_EventDetails);
        List<String> request_display = new ArrayList();
        request_display.add("eventall");

        request_display = (List<String>) sess.getAttribute(sg.com.Shange.Utils.Sess_SearchVolunteerRequestRedis);

        System.out.println("req dis: " + request_display);
        if (request_display == null) {

            request_display = new ArrayList<String>();
            request_display.add("Request Status");
            sess.setAttribute(sg.com.Shange.Utils.Sess_SearchVolunteerRequestRedis, request_display);
            System.out.println("Start to display");
        }

        String redisid = eventall;
        EventDetails edindiv = new EventDetails();
        int current_found_items = -1;

        for (int i = 0; i < ed.size(); i++) {
            if (ed.get(i).getStrEventId().equalsIgnoreCase(redisid)) {
                edindiv = ed.get(i);
                current_found_items = i;
                break;
            }
        }
        System.out.println(current_found_items + " index");
        if (current_found_items >= 0) {
            allEvent aEvent = new allEvent();

            aEvent.setDescription(edindiv.getStrDescription());
            aEvent.setUserName(edindiv.getStrUsername());
            aEvent.setRedisID(redisid);
            aEvent.setEventDate(edindiv.getStrEventFrom());
            aEvent.setTitle(edindiv.getStrTitle());
            aEvent.setRequest(registerName.getUserName());
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = today.format(formatter);
            aEvent.setRequestDate(formattedDate);
            System.out.println("Check exist: " + aEvent);
            boolean result = userservice.Check_UserNewEventRedisRequestExist(aEvent);
            if (result) {
                request_display.add(edindiv.getStrTitle() + " already requested before!!!");
            } else {
                request_display.add(edindiv.getStrTitle() + " sent request now.");
                System.out.println("allevent: " + aEvent);
                result = userservice.insertNewEventRedisRequestManualTx(aEvent);
            }
        }
        ed.remove(current_found_items);
        sess.setAttribute(sg.com.Shange.Utils.Sess_EventDetails, ed);
        mav.addObject("myEventDetails", ed);
        mav.addObject("myRequest", request_display);
        mav.addObject("myType", "Welcome back Volunteer");
        mav.addObject("regeventName", registerName.getFullName());
        mav.addObject("regeventDate", LocalDate.now());
        return mav;

    }

    @PostMapping("/insertRequestMapEvent")
    public ModelAndView insertRequestMapEvent(@RequestParam("eventall") String eventall1, HttpSession sess) {

        ModelAndView mav = new ModelAndView("homepageVolunteer");
        System.out.println("The eventid= " + eventall1);
        int eventall = Integer.parseInt(eventall1);

        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);

        List<MapRequestModify> reqStatus = (List<MapRequestModify>) sess.getAttribute(sg.com.Shange.Utils.Sess_SearchVolunteerMapRequestStatus);

        List<String> request_display = new ArrayList();
        request_display.add("eventall");
        request_display = (List<String>) sess.getAttribute(sg.com.Shange.Utils.Sess_SearchVolunteerRequestSQL);
        System.out.println("req dis: " + request_display);
        if (request_display == null) {

            request_display = new ArrayList<String>();
            request_display.add("Request Status");
            sess.setAttribute(sg.com.Shange.Utils.Sess_SearchVolunteerRequestSQL, request_display);
            System.out.println("Start to display");
        }

        boolean result = userservice.updateEventSqlReplyByIDFullChecking(eventall, registerName.getUserName());
        System.out.println(result);
        if (result) {

            MapRequestModify edindiv = new MapRequestModify();
            int current_found_items = -1;

            for (int i = 0; i < reqStatus.size(); i++) {
                if (reqStatus.get(i).getId() == eventall) {
                    edindiv = reqStatus.get(i);
                    String theReply = edindiv.getReply();
                    theReply += registerName.getUserName() + ", ";
                    edindiv.setReply(theReply);
                    reqStatus.set(i, edindiv);
                    current_found_items = i;
                    break;
                }
            }
        } else {
            
        }

        mav.addObject("myRequestExternalStatus", reqStatus);
        String myUsername = registerName.getUserName()+",";
        mav.addObject("myUserName", myUsername);

        return mav;

    }

    public EventDetails convertFunction(String eventall) {
        
        System.out.println("convert function entry ");
        eventall = eventall.substring(13, eventall.length() - 1);
        System.out.println("convert function entry " + eventall);
        String[] keyValuePairs = eventall.split(", ");

        EventDetails eventDetails = new EventDetails();
        for (String pair : keyValuePairs) {

            String[] keyValue = pair.split("=");
            String key = keyValue[0].trim();
            String value = "";
            if (keyValue.length > 1) {
                value = keyValue[1].trim();
            }
            System.out.println(key + " " + value);
            switch (key) {
                case "strEventId":
                    eventDetails.setStrEventId(value);
                    break;
                case "strUsername":
                    eventDetails.setStrUsername(value);
                    break;
                case "strEventtype":
                    eventDetails.setStrEventtype(value);
                    break;
                case "strTitle":
                    eventDetails.setStrTitle(value);
                    break;
                case "strDescription":
                    eventDetails.setStrDescription(value);
                    break;
                case "strPhysical":
                    eventDetails.setStrPhysical(value);
                    break;
                case "strOnline":
                    eventDetails.setStrOnline(value);
                    break;
                case "strNorth":
                    eventDetails.setStrNorth(value);
                    break;
                case "strSouth":
                    eventDetails.setStrSouth(value);
                    break;
                case "strEast":
                    eventDetails.setStrEast(value);
                    break;
                case "strWest":
                    eventDetails.setStrWest(value);
                    break;
                case "strCentral":
                    eventDetails.setStrCentral(value);
                    break;
                case "strSampleFood":
                    eventDetails.setStrSampleFood(value);
                    break;
                case "strSampleFoodSite1":
                    eventDetails.setStrSampleFoodSite1(value);
                    break;
                case "strSampleFoodSite2":
                    eventDetails.setStrSampleFoodSite2(value);
                    break;
                case "strQuiz":
                    eventDetails.setStrQuiz(value);
                    break;
                case "strImage":
                    eventDetails.setStrImage(value);
                    break;
                case "strFile":
                    eventDetails.setStrFile(value);
                    break;
                case "strEventFrom":
                    eventDetails.setStrEventFrom(value);
                    break;
                case "strEventTo":
                    eventDetails.setStrEventTo(value);
                    break;
                case "strTimeFrom":
                    eventDetails.setStrTimeFrom(value);
                    break;
                case "strTimeTo":
                    eventDetails.setStrTimeTo(value);
                    break;
                case "strVolunteer":
                    eventDetails.setStrVolunteer(value);
                    break;

                default:
                    break;
            }
        }
        return eventDetails;
    }

    @PostMapping("/insertRequestEventConfirm")
    public ModelAndView insertRequestEventConfirm(@RequestParam("eventall") String eventall1, HttpSession sess) {

        ModelAndView mav = new ModelAndView("homepageOrganisation");
        System.out.println("The eventid= " + eventall1);
        int eventall = Integer.parseInt(eventall1);

        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);

        List<allEvent> reqStatus = (List<allEvent>) sess.getAttribute(sg.com.Shange.Utils.Sess_SearchVolunteerMapConfirmStatus);

        int current_found_items = -1;
        allEvent edindiv = new allEvent();
        for (int i = 0; i < reqStatus.size(); i++) {
            if (reqStatus.get(i).getId() == eventall) {
                edindiv = reqStatus.get(i);
                edindiv.setConfirm(registerName.getUserName());
                reqStatus.set(i, edindiv);
                current_found_items = i;
                break;
            }
        }

        boolean result = userservice.updateEventRedisRequestToConfirm(edindiv);
        System.out.println(result);

        mav.addObject("myInternalRequestStatus", reqStatus);
        mav.addObject("myUserName", registerName.getUserName());
        sess.setAttribute(sg.com.Shange.Utils.Sess_SearchVolunteerMapConfirmStatus, reqStatus);
        return mav;

    }


}