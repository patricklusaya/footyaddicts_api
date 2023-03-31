package com.example.footyaddicts.controller;

import com.example.footyaddicts.dto.requests.LoginRequest;
import com.example.footyaddicts.dto.requests.RegistrationRequest;
import com.example.footyaddicts.dto.response.AuthenticationResponse;
import com.example.footyaddicts.dto.response.MessageResponse;
import com.example.footyaddicts.repos.RoleRepository;
import com.example.footyaddicts.repos.StaffRepository;
import com.example.footyaddicts.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.validation.Valid;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    public StaffRepository staffRepository;
    @Autowired
    public RoleRepository roleRepository;
    @Autowired
    public AuthService authService;


    @PostMapping("/register-staff")

    public ResponseEntity<?> registerEmployee( @Valid  @RequestBody RegistrationRequest registrationRequest){
      if (staffRepository.existsByUsername(registrationRequest.getUsername())){
          return ResponseEntity.badRequest().body(new MessageResponse("Employee with that name already Exists"));
      }
        if (staffRepository.existsByEmail(registrationRequest.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Email already taken"));
        }
        authService.signup(registrationRequest);

return  ResponseEntity.ok().body(new MessageResponse("Staff registered successfully"));
    }

    @PostMapping("/signin")
    public AuthenticationResponse SignIn(@Valid @RequestBody LoginRequest loginRequest){
        return authService.signin(loginRequest);
    }

}
