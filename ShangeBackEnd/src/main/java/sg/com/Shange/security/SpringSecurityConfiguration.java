package sg.com.Shange.security;
import static org.springframework.security.config.Customizer.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    List<UserDetails> users = new ArrayList<>();
    private InMemoryUserDetailsManager userDetailsManager; //put new user into memory

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Value("${spring.cors}")
	private String springUser;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        Arrays.stream(springUser.split(",")).forEach(config::addAllowedOrigin);


        // Adjust origin as needed
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public InMemoryUserDetailsManager createUserDetailsManager() {
        userDetailsManager = new InMemoryUserDetailsManager(createUsers());
        return userDetailsManager;
    }

    private List<UserDetails> createUsers() {
        
        return users;
    }

    public UserDetails createNewUser(String username, String password, String... roles) {
        Function<String, String> passwordEncoder = input -> passwordEncoder().encode(input);
        UserDetails myDetails = User.builder()
                .passwordEncoder(passwordEncoder)
                .username(username)
                .password(password)
                .roles(roles)
                .build();
        return myDetails;
    }

    public void addUser(String username, String password, String... roles) {
        UserDetails userDetails = createNewUser(username, password, roles);
        userDetailsManager.createUser(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults());

        http
        .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                    //    .requestMatchers(HttpMethod.GET, "/api/").permitAll() 
                    //    .requestMatchers(HttpMethod.GET, "/login/").permitAll()
                    //    .requestMatchers(HttpMethod.POST, "/login/").permitAll()
                    // .requestMatchers(HttpMethod.POST, "/api/").permitAll()  
                        .requestMatchers("/authenticate").permitAll()
                        .anyRequest().authenticated()
               )
               
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));
        
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    

}