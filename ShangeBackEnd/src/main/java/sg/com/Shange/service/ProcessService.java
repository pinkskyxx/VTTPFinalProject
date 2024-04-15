package sg.com.Shange.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProcessService {
    
    String url_booksearch1 = "https://openlibrary.org/search.json?q=";
    String url_Mealsearch2 = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
    

        RestTemplate template = new RestTemplate();

    public String MealSearch(String food) {

        String url_Mealsearch = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
        url_Mealsearch += food;
        System.out.println(url_Mealsearch);
        String result = template.getForObject(url_Mealsearch, String.class);

        return result;

    }
}
