package luke.shopbackend.controller;

import luke.shopbackend.model.AuthenticationBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BasicAuthenticationController {

//    @GetMapping(path = "/user")
//    public AuthenticationBean authenticationBean(){
//        return new AuthenticationBean("Jesteś uwierzytelniony");
//    }

    @GetMapping(path = "/user")
    public Principal user(Principal user){
        return user;
    }
}
