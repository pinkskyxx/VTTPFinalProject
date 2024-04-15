package sg.com.Shange.controller;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.com.Shange.models.Meal;
import sg.com.Shange.service.ProcessService;


@RestController
@RequestMapping("/api")
public class MealsController {

    @Autowired
    ProcessService processSvc;

    @PostMapping(path = "/{category}", produces = "application/json")
    public String processRecipesSearch(@PathVariable String category) {


        String result = "";
        if (category.equals("searchRecipes")) {
            result = processSvc.MealSearch("");
        }

        return result;
    }

    @PostMapping(path = "/search/{site}", produces = "application/json")
    public String searchByMealName(@PathVariable String site,
            @RequestParam(name = "searchName", required = false) String searchName) {

        System.out.println(searchName);
        String result = processSvc.MealSearch(searchName);

        return result;
    }

    @GetMapping(path = "/search/{site}", produces = "application/json")
    public String getsearchByMealName(@PathVariable String site,
            @RequestParam(name = "searchName", required = false) String searchName) {

        System.out.println(site);
        String result = processSvc.MealSearch(site);

        return result;
    }


}
