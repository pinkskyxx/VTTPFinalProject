package sg.com.Shange.controller;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import sg.com.Shange.models.EventDetails;
import sg.com.Shange.models.MapRequest;
import sg.com.Shange.models.MapRequestModify;
import sg.com.Shange.models.UserRegister;
import sg.com.Shange.models.allEvent;
import sg.com.Shange.service.DatabaseService;
import sg.com.Shange.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;

@Controller
@CrossOrigin
public class registerController {

    @Autowired
    private UserService userservice;

    @Autowired
    private DatabaseService databaseService;

    @Value("${s3.image.location}")
    private String ImageLocation;

    @PostMapping("/api/signUpOrganisation")
    public ResponseEntity<String> signUpOrganisation(@RequestBody UserRegister details) {
        
        System.out.println("Receive Post Sign up Organization");
       
        System.out.println(details);
     

        Class<?> clazz = details.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // allows accessing private fields
            try {
                Object value = field.get(details);
                System.out.println(field.getName() + ": " + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // Check_UserExist
        boolean result1 = userservice.Check_UserExist(details);
        System.out.println("Check username result = " + result1);
        if (result1) {
            String errorMessage = "Login Name already exists";
            String json = "{ \"result\":\"" + errorMessage + "\" }";
            System.out.println("Json: " + json);
            return ResponseEntity.badRequest().body(json);
        } else {
            boolean result = userservice.createNewUserManualTx(details);
            System.out.println("insert result = " + result);
            System.out.println();
            UserRegister userRegister = details;
            System.out.println("Organization Register: " + userRegister);
            System.out.println();
            if (result) {
                String successMessage = "Organization registered successfully";
                String json = "{ \"result\":\"" + successMessage + "\" }";
                System.out.println("Json: " + json);
                return ResponseEntity.ok(json);
            } else {
                String errorMessage = "Login Name already exists";
                String json = "{ \"result\":\"" + errorMessage + "\" }";
                System.out.println("Json: " + json);
                return ResponseEntity.badRequest().body(json);
            }

        }

    }

    @PostMapping("/api/signUpVolunteer")
    public ResponseEntity<String> signUpVolunteer(@RequestBody UserRegister details) {
        // Access details properties directly
        System.out.println("Receive Post Sign up Volunteer");
        
        System.out.println("Post Volunteer" + details);
       
        Class<?> clazz = details.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(details);
                System.out.println(field.getName() + ": " + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Check_UserExist
        boolean result1 = userservice.Check_UserExist(details);
        System.out.println("Check username result = " + result1);
        if (result1) {
            String errorMessage = "Login Name already exists";
            String json = "{ \"result\":\"" + errorMessage + "\" }";
            System.out.println("Json: " + json);
            return ResponseEntity.badRequest().body(json);
        } else {
            boolean result = userservice.createNewUserManualTx(details);
            System.out.println("insert result = " + result);
            System.out.println();
            UserRegister userRegister = details;
            System.out.println("userRegister: " + userRegister);
            System.out.println();
            if (result) {
                String successMessage = "Volunteer registered successfully";
                String json = "{ \"result\":\"" + successMessage + "\" }";
                System.out.println("Json: " + json);
                return ResponseEntity.ok(json);
            } else {
                String errorMessage = "Login Name already exists";
                String json = "{ \"result\":\"" + errorMessage + "\" }";
                System.out.println("Json: " + json);
                return ResponseEntity.badRequest().body(json);
            }

        }
    }

    @PostMapping("/api/mapEventRequest")
    public ResponseEntity<String> mapEventRequest(@RequestBody List<MapRequest> detailsList) {
        // Access details properties directly
        System.out.println("Receive Post map event request");
        for (MapRequest details : detailsList) {
            System.out.println("Post event " + details);
            boolean result = userservice.createNewMapEventRequestManualTx(details);
            if (result == false) {
                String errorMessage = "Save Request error";
                String json = "{ \"result\":\"" + errorMessage + "\" }";
                System.out.println("Json: " + json);
                return ResponseEntity.badRequest().body(json);
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for (MapRequest mapRequest : detailsList) {
                allEvent ae = new allEvent();
                int theID = userservice.getEventSqlId(mapRequest.getMyid(), mapRequest.getTitle());
                ae.setMysqlID(theID);
                ae.setUserName(mapRequest.getMyid());
                ae.setAddress(mapRequest.getAddress());
                ae.setTitle(mapRequest.getTitle());
                ae.setDescription(mapRequest.getDescription());
                ae.setRequestDate(formatter.format(mapRequest.getRequestDate()));
                ae.setEventDate(formatter.format(mapRequest.getEventDate()));
                System.out.println("ae is: " + ae);
                userservice.insertNewEventSQLManualTx(ae);
            }

        }

        String successMessage = "Event Request successfully recorded";
        String json = "{ \"result\":\"" + successMessage + "\" }";
        System.out.println("Json: " + json);
        return ResponseEntity.ok(json);
    }

    @GetMapping("/api/mapEventRequestModify")
    public ResponseEntity<List<MapRequestModify>> getMapEventRequests(
            @RequestParam(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

        System.out.println("Enter Get mapEventRequestModify");
        System.out.println("fromDate: " + fromDate);
        System.out.println("toDate: " + toDate);
        List<MapRequestModify> mapEventRequests = userservice.findByEventDateBetween(fromDate, toDate);
        System.out.println("return result: " + mapEventRequests);
        return ResponseEntity.ok(mapEventRequests);
    }

    @GetMapping("/api/EventDetailsAll")
    public ResponseEntity<List<EventDetails>> getEventDetailsAll() {

        System.out.println("Enter Get EventDetailsAll");
        
        List<EventDetails> mapEventRequests = databaseService.getAllEvent();
        for (EventDetails eventDetails : mapEventRequests) {
            String img = eventDetails.getStrImage();
            if (img != "" && img != null) {
                if (!img.contains(ImageLocation)) {
                    img = ImageLocation + img;
                    eventDetails.setStrImage(img);
                }
            }
            String filename = eventDetails.getStrFile();
            if (filename != "" && filename != null) {
                if (!filename.contains(ImageLocation)) {
                    filename = ImageLocation + filename;
                    eventDetails.setStrFile(filename);
                }
            }
        }
        System.out.println("return result: " + mapEventRequests);
        return ResponseEntity.ok(mapEventRequests);
    }

    @DeleteMapping("/api/mapEventRequestModify/delete/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable int id) {
        System.out.println("Delete request entey");
        System.out.println(id);
        try {
            boolean result = userservice.deleteMapEventModifyManualTx(id);
            if(result){
                String successMessage = "Successfully Deleted";
                String json = "{ \"result\":\"" + successMessage + "\" }";
                System.out.println("Json: " + json);
                return ResponseEntity.ok(json);
            }else{
                String errorMessage = "Failed to delete record";
                String json = "{ \"result\":\"" + errorMessage + "\" }";
                System.out.println("Json: " + json);
                return ResponseEntity.badRequest().body(json);
                // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete record");
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete record");
        }
    }

    @PutMapping("/api/mapEventRequestModify/update/{id}")
    public ResponseEntity<String> updateRecord(@PathVariable int id, @RequestBody MapRequestModify updatedData) {

        System.out.println("Put request entey");
        System.out.println(id + " " + updatedData);
        try {
            boolean result = userservice.updateMapEventModifyManualTx(id, updatedData);
            if(result){
                String successMessage = "Record updated successfully";
                String json = "{ \"result\":\"" + successMessage + "\" }";
                System.out.println("Json: " + json);
                return ResponseEntity.ok(json);
            }else{
                String errorMessage = "Failed to update record";
                String json = "{ \"result\":\"" + errorMessage + "\" }";
                System.out.println("Json: " + json);
                return ResponseEntity.badRequest().body(json);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update record: " + e.getMessage());
        }
    }


}
