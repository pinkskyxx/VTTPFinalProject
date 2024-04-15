package sg.com.Shange.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sg.com.Shange.security.AuthenticationRequest;
import sg.com.Shange.security.AuthenticationResponse;
import sg.com.Shange.security.JwtUtil;

@RestController
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${spring.AuthenticationRequest.USER}")
	private String springUser;

	@Value("${spring.AuthenticationRequest.PW}")
	private String springPW;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            // Authenticate with hardcoded username and password
            if ( springUser.equals(authenticationRequest.getUsername()) && springPW.equals(authenticationRequest.getPassword())) {
                final String jwt = jwtUtil.generateToken(authenticationRequest.getUsername());
                return ResponseEntity.ok(new AuthenticationResponse(jwt));
            } else {
                throw new Exception("Incorrect username or password");
            }
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }
    }
}
