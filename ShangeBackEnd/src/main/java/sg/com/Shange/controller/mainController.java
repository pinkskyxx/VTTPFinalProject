package sg.com.Shange.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Size;
import sg.com.Shange.Utils;
import sg.com.Shange.models.EventDetails;
import sg.com.Shange.models.MapRequestModify;
import sg.com.Shange.models.ProfileJointUser;
import sg.com.Shange.models.UserProfile;
import sg.com.Shange.models.UserRegister;
import sg.com.Shange.models.allEvent;
import sg.com.Shange.service.UserService;

@Controller
public class mainController {


    @Value("${s3.image.location}")
    private String ImageLocation;

    @Value("${spring.redirectUri}")
    private String redirectUri;

    @Autowired
    private UserService userservice;

    private static Integer countattp = 0;

    @RequestMapping(value = { "/", "/index", "/login" }, method = RequestMethod.GET)
    public String showLoginPage(Model model, HttpSession sess) {

        countattp++;
        System.out.println("Enter login get" + countattp);

        // String name = (String) model.getAttribute("username");
        // System.out.println("name: " + name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String myid = authentication.getName();
        System.out.println(authentication.getDetails());
        System.out.println(myid);
        // model.addAttribute("ExtUrl", redirectUri);
        System.out.println(redirectUri + "/SignUpVolunteer");
        System.out.println(redirectUri + "/SignUpOrganisation");

        model.addAttribute("signUpVolunteerUrl", redirectUri + "/SignUpVolunteer");
        model.addAttribute("signUpOrganisationUrl", redirectUri + "/SignUpOrganisation");
        if (myid.equals("anonymousUser"))
            return "login";

        System.out.println("Now getting user info ");
        UserRegister registerUser = userservice.Get_UserInfo(myid);
        System.out.println("User data: " + registerUser);
        if (registerUser == null) {
            return "login";
        }
        // model.addAttribute("regeventuserName", registerUser.getUserName());
        model.addAttribute("regeventName", registerUser.getFullName());
        model.addAttribute("regeventDate", LocalDate.now());
        
        sess.setAttribute(sg.com.Shange.Utils.Sess_name, registerUser);
        boolean ProfileExist = userservice.Check_ProfileExist(myid);
        UserProfile Profile = new UserProfile();
        System.out.println("name: " + myid);
        Profile.setUserName(myid);
        sess.setAttribute(sg.com.Shange.Utils.Sess_Profile, Profile);
        Profile = (UserProfile) sess.getAttribute(sg.com.Shange.Utils.Sess_Profile);
        if (ProfileExist) {
            Profile = userservice.Get_UserProfileInfo(myid);
            System.out.println("Get profile: " + Profile);
            sess.setAttribute(sg.com.Shange.Utils.Sess_Profile, Profile);
            // sess.setAttribute(sg.com.Shange.Utils.Sess_Profile, volunteerProfile);
            // String image_fileName = Profile.getImage();
            if (Profile.getImage() != "") {
                model.addAttribute("StrImageDisplay", ImageLocation + Profile.getImage());
                System.out.println("Image path: " + ImageLocation + Profile.getImage());
            }

        }

        model.addAttribute("myVolunteerProfile", Profile);
        System.out.println("myVol: " + Profile);
        String googlecalendar = (String) sess.getAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_login);
        System.out.println(googlecalendar);
        if (googlecalendar == null || googlecalendar=="false") {
            sess.setAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_login, "false");
            model.addAttribute("sessionGoogleCalendar", "false");
        }
        // sess.setAttribute("sessionGoogleCalendar", false);

        System.out.println("Set attribute");
        if (registerUser.getType().equals("volunteer")) {
            System.out.println("mytype " + "volunteer");
            return "homepageVolunteer";
        } else if (registerUser.getType().equals("organization")) {
            System.out.println("mytype " + "Organization");
            return "homepageOrganisation";
        } else
            return "homepageAdmin";
        // return "homepage";
    }

    @GetMapping("/profile")
    public String showhomepage(Model model, HttpSession sess) {
        System.out.println("profile");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        System.out.println(registerName);

        if (registerName == null) {
            // model.addAttribute("ExtUrl", redirectUri);
            model.addAttribute("signUpVolunteerUrl", redirectUri + "/SignUpVolunteer");
            model.addAttribute("signUpOrganisationUrl", redirectUri + "/SignUpOrganisation");
            return "login";
        }
        model.addAttribute("regeventName", registerName.getFullName());
        model.addAttribute("regeventDate", LocalDate.now());
        model.addAttribute("userProfile", registerName);
        UserProfile myProfile = (UserProfile) sess.getAttribute(sg.com.Shange.Utils.Sess_Profile);
        if (myProfile.getImage() != "") {
            model.addAttribute("StrImageDisplay", ImageLocation + myProfile.getImage());
        }
        String type = registerName.getType();
        model.addAttribute("volProfile", myProfile);
        System.out.println("Before create profile: " + myProfile);
        if (type.equals("volunteer")) {
            // UserProfile volunteerProfile = new UserProfile();
            // volunteerProfile.s(type);(registerName.getFullName());
            // volunteerProfile.setUserName(registerName.getUserName());
            // volunteerProfile.setUserType(registerName.getType());

            return "createVolunteerProfile";
        } else {
            // OrganisationProfile volunteerProfile = new OrganisationProfile();
            // volunteerProfile.setFullName(registerName.getFullName());
            // volunteerProfile.setUserName(registerName.getUserName());
            // volunteerProfile.setUserType(registerName.getType());

            return "createOrganisationProfile";
        }

        // return "homepage";
    }

    @PostMapping(value = { "/createVolunteerProfile", "/createOrganisationProfile" })
    public String postCreateVolounteerProfile(
            @RequestParam String skills,
            @RequestParam String availability,
            @RequestParam String pastParticipation,
            @RequestParam String clientId,
            @RequestParam String clientSecret,
            @RequestParam("image") MultipartFile imageFile,
            HttpSession sess, Model model) throws IOException {
        // TODO: process POST request
        System.out.println("Post createVolunteerProfile");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        System.out.println(registerName);

        if (registerName == null) {
            // model.addAttribute("ExtUrl", redirectUri);
            model.addAttribute("signUpVolunteerUrl", redirectUri + "/SignUpVolunteer");
            model.addAttribute("signUpOrganisationUrl", redirectUri + "/SignUpOrganisation");
            return "login";
        }
        String imageFileName = "";
        if (!imageFile.isEmpty()) {
            // imageFileName = saveFile(imageFile);
        }
        UserProfile myProfile = (UserProfile) sess.getAttribute(sg.com.Shange.Utils.Sess_Profile);
        System.out.println("createVolunteerProfile profile: " + myProfile);
        // VolunteerProfile volunteerProfile = new VolunteerProfile();
        // volunteerProfile.setFullName(registerName.getFullName());
        // volunteerProfile.setUserName(registerName.getUserName());
        // volunteerProfile.setUserType(registerName.getType());
        // volunteerProfile.setImage(imageFileName);

        myProfile.setSkills(skills);
        myProfile.setAvailability(availability);
        myProfile.setPastParticipation(pastParticipation);
        myProfile.setClientId(clientId);
        myProfile.setClientSecret(clientSecret);
        String imageName = myProfile.getImage();

        model.addAttribute("myVolunteerProfile", myProfile);
        model.addAttribute("regeventName", registerName.getFullName());
        model.addAttribute("regeventDate", LocalDate.now());
        System.out.println(sg.com.Shange.Utils.Sess_Profile);
        // String image_fileName = volunteerProfile.getImage();
        System.out.println("imageName: " + imageFile);
        String filename = "";
        // userservice
        // imageFile
        try {
            if (!imageFile.isEmpty()) {
                filename = userservice.postPicture(imageFile);
                System.out.println("filename: " + filename);
                myProfile.setImage(filename);
                model.addAttribute("StrImageDisplay", ImageLocation + filename);

            } else {
                if (!imageName.isEmpty()) {
                    model.addAttribute("StrImageDisplay", ImageLocation + imageName);
                }

            }
        } catch (Exception e) {
        }
        if (userservice.Check_ProfileExist(registerName.getUserName())) {
            userservice.updateProfileManualTx(myProfile);
        } else
            userservice.createNewProfileManualTx(myProfile);
        // String imageName = UUID.randomUUID().toString();
        // imageRepository.putObject(picture, imageName);
        // news.setImage(imageName);

        if (registerName.getType().equals("volunteer"))
            return "homepageVolunteer";
        else
            return "homepageOrganisation";
    }

    @GetMapping("/home")
    public String showHome(Model model, HttpSession sess) throws IOException {

        System.out.println("home");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        System.out.println(registerName);
        model.addAttribute("signUpVolunteerUrl", redirectUri + "/SignUpVolunteer");
        model.addAttribute("signUpOrganisationUrl", redirectUri + "/SignUpOrganisation");
        if (registerName == null) {
            return "login";
        }
        model.addAttribute("regeventName", registerName.getFullName());
        model.addAttribute("regeventDate", LocalDate.now());
        model.addAttribute("userProfile", registerName);
        UserProfile Profile = (UserProfile) sess.getAttribute(sg.com.Shange.Utils.Sess_Profile);

        if (Profile.getImage() != "") {
            model.addAttribute("StrImageDisplay", ImageLocation + Profile.getImage());

        }
        String type = registerName.getType();

        model.addAttribute("myVolunteerProfile", Profile);
        System.out.println("myVol: " + Profile);
        String googlecalendar = (String) sess.getAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_login);
        System.out.println(googlecalendar);
        if (googlecalendar == null || googlecalendar=="false") {
            sess.setAttribute(sg.com.Shange.Utils.Sess_GoogleCalendar_login, "false");
            model.addAttribute("sessionGoogleCalendar", "false");
        }
        if (type.equals("volunteer")) {
            System.out.println("mytype " + "volunteer");

            return "homepageVolunteer";
        } else {
            System.out.println("mytype " + "Organization");
            List<EventDetails> eventDetails = (List<EventDetails>)sess.getAttribute(Utils.Sess_EventDetails);
            if(eventDetails!=null)
                model.addAttribute("myEventDetails", eventDetails);
            return "homepageOrganisation";
        }

    }

    @GetMapping("/create")
    public String showcreatenewPage(Model model, HttpSession sess) {
        System.out.println("Create new Event");

        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        System.out.println(registerName);
        if (registerName == null) {
            // model.addAttribute("ExtUrl", redirectUri);
            model.addAttribute("signUpVolunteerUrl", redirectUri + "/SignUpVolunteer");
            model.addAttribute("signUpOrganisationUrl", redirectUri + "/SignUpOrganisation");
            return "login";
        }
        model.addAttribute("regeventName", registerName.getFullName());
        model.addAttribute("regeventDate", LocalDate.now());
        return "createNewEvent";
    }

    @GetMapping("/Foodschedule")
    public String showFoodSchedule(Model model, HttpSession sess) {
        System.out.println("Create new Food schedule");
        model.addAttribute("dateOfFoodEvent", LocalDate.now());

        // sess.getAttributeNames(sg.com.Shange.Utils.Sess_name, registerName);
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        if (registerName == null) {
            // model.addAttribute("ExtUrl", redirectUri);
            model.addAttribute("signUpVolunteerUrl", redirectUri + "/SignUpVolunteer");
            model.addAttribute("signUpOrganisationUrl", redirectUri + "/SignUpOrganisation");
            return "login";
        }
        model.addAttribute("regeventName", registerName.getFullName());
        model.addAttribute("regeventDate", LocalDate.now());
        return "homepageOrganisationFoodSearch";
    }

    @GetMapping("/searchvolunteers")
    public ModelAndView showvolunteerdatabasev(HttpSession sess) {

        // return "homepageOrganisationSearchVolunteer";

        System.out.println("searchvolunteers");


        
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        System.out.println(registerName);
        // if (registerName == null) {
        //     return "login";
        // }


        ModelAndView mav = new ModelAndView("homepageOrganisationSearchVolunteer");

        System.out.println("mytype: "+registerName.getType());
        if(registerName.getType().equals("volunteer"))
        {
            mav = new ModelAndView("homepageVolunteerSearchVolunteer");
            mav.addObject("myType", "Welcome back Volunteer");
        }
        else
            mav.addObject("myType", "Welcome back Organisation");

        mav.addObject("regeventName", registerName.getFullName());
        mav.addObject("regeventDate", LocalDate.now());

        // List<UserProfile> vol = userservice.Get_AllProfileInfoType("volunteer");
        List<ProfileJointUser> volJoint = userservice.Get_AllProfileInfoJointType("volunteer");
        System.out.println("vol joint: "+ volJoint);
        for (ProfileJointUser iterable_element : volJoint) {
            
            String img =  iterable_element.getImage();
            if(img!="" && img!=null)
            {
                if(!img.contains(ImageLocation))
                {
                    img = ImageLocation + img;
                    iterable_element.setImage(img);
                }
            }
        }
        sess.setAttribute(Utils.Sess_Profile_volunteer_joint, volJoint);
        mav.addObject("myVolunteerProfile", volJoint);
        // if (eventDetails != null) {
            
            
        // }

        return mav;
    }

    @GetMapping("/acceptvolunteers")
    public ModelAndView showinternalvolunteereventenrol(HttpSession sess) {

        System.out.println("Show searchvolunteers enrol to confirm");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        System.out.println(registerName);

        ModelAndView mav = new ModelAndView("homepageOrganisation");

        mav.addObject("myType", "Welcome back Organisation");
        mav.addObject("regeventName", registerName.getFullName());
        mav.addObject("regeventDate", LocalDate.now());

        // List<UserProfile> vol = userservice.Get_AllProfileInfoType("volunteer");
        List<allEvent> reqStatus = userservice.Get_AllNewEventVolunteerToConfirm(registerName.getUserName());

        // sess.setAttribute(Utils.Sess_Profile_volunteer_joint, volJoint);
        mav.addObject("myInternalRequestStatus", reqStatus);
        // if (eventDetails != null) {
            
            
        // }
        mav.addObject("myUserName", registerName.getUserName());
        sess.setAttribute(sg.com.Shange.Utils.Sess_SearchVolunteerMapConfirmStatus, reqStatus);
        return mav;
    }
    @GetMapping("/searchvolunteerseventenrol")
    public ModelAndView showvolunteereventenrol(HttpSession sess) {

        System.out.println("searchvolunteers enrol");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        System.out.println(registerName);

        ModelAndView mav = new ModelAndView("homepageVolunteer");

        mav.addObject("myType", "Welcome back Volunteer");
        mav.addObject("regeventName", registerName.getFullName());
        mav.addObject("regeventDate", LocalDate.now());

        // List<UserProfile> vol = userservice.Get_AllProfileInfoType("volunteer");
        List<allEvent> reqStatus = userservice.Get_UserNewEventOwnRequest(registerName.getUserName());

        // sess.setAttribute(Utils.Sess_Profile_volunteer_joint, volJoint);
        mav.addObject("myRequestStatus", reqStatus);
        // if (eventDetails != null) {
            
            
        // }

        return mav;
    }

    @GetMapping("/searchExternaleventenrol")
    public ModelAndView showvolunteerExternaleventenrol(HttpSession sess) {

        System.out.println("searchExternal event enrol");
        UserRegister registerName = (UserRegister) sess.getAttribute(sg.com.Shange.Utils.Sess_name);
        System.out.println(registerName);

        ModelAndView mav = new ModelAndView("homepageVolunteer");
        
        mav.addObject("myType", "Welcome back Volunteer");
        mav.addObject("regeventName", registerName.getFullName());
        mav.addObject("regeventDate", LocalDate.now());

        List<MapRequestModify> reqStatus = userservice.GetAllMapEventAll();
        // System.out.println(reqStatus);

        mav.addObject("myRequestExternalStatus", reqStatus);
        String myUsername = registerName.getUserName()+",";
        mav.addObject("myUserName", myUsername);
        sess.setAttribute(sg.com.Shange.Utils.Sess_SearchVolunteerMapRequestStatus, reqStatus);
        // if (eventDetails != null) {
            
            
        // }

        return mav;
    }


    @GetMapping("/createquiz")
    public String showcreatequiz(Model model) {
        return "createquiz";
    }



}
