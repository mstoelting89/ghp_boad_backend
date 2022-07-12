package com.example.system.security.authentication;

import com.example.system.email.EmailService;
import com.example.system.security.jwt.JwtTokenDto;
import com.example.system.user.User;
import com.example.system.user.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(path = "/api/v1/login")
public class AuthenticationController {

    private AuthenticationService authenticationService;
    private UserServiceImpl userServiceImpl;
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthenticationDto authenticationDto) {

        JwtTokenDto token = authenticationService.generateJwtToken(authenticationDto.getEmail(), authenticationDto.getPassword());
        User user = userServiceImpl.loadUserByMail(authenticationDto.getEmail());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(
                token.getToken(),
                user.getAuthorities(),
                authenticationDto.getEmail()
        );

        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/password/reset")
    @ResponseBody
    public String resetPassword() {

        return "foo";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handlerEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
