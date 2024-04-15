package sg.com.Shange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.com.Shange.service.UserService;

@SpringBootApplication
public class ShangeApplication implements CommandLineRunner {

	@Autowired
	private UserService userservice;

	public static void main(String[] args) {
		SpringApplication.run(ShangeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("System start");
		userservice.InitUserToMain();
	}

}
