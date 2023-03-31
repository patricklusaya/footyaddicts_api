package com.example.footyaddicts.service;



import com.example.footyaddicts.dto.requests.LoginRequest;
import com.example.footyaddicts.dto.requests.RegistrationRequest;
import com.example.footyaddicts.dto.response.AuthenticationResponse;
import com.example.footyaddicts.models.ERole;
import com.example.footyaddicts.models.Role;
import com.example.footyaddicts.models.Staff;
import com.example.footyaddicts.repos.RoleRepository;
import com.example.footyaddicts.repos.StaffRepository;


import com.example.footyaddicts.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthService {
    public PasswordEncoder encoder;
    public RoleRepository roleRepository;
    public StaffRepository staffRepository;

    @Autowired
    private  final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtUtils jwtUtils;
    public void signup(RegistrationRequest registrationRequest) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
        String memberSince = now.format(formatter);
       Staff staff = new Staff();
       staff.setEmail(registrationRequest.getEmail());
       staff.setUsername(registrationRequest.getUsername());
       staff.setName(registrationRequest.getName());
       staff.setMemberSince(memberSince);
       staff.setPassword(encoder.encode("123456"));

        Set<String> strRoles = registrationRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role writerRole = roleRepository.findByName(ERole.ROLE_WRITER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(writerRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "moderator":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role moderator is not found."));
                        roles.add(adminRole);
                        break;


                    default:
                        Role writerRole = roleRepository.findByName(ERole.ROLE_WRITER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(writerRole);
                }
            });
        }
        staff.setRoles(roles);
        staffRepository.save(staff);

    }

    public AuthenticationResponse signin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
//        String name = loginRequest.getName();
//        if(name == null || name.isEmpty()){
//            Optional<Staff> staff = staffRepository.findByUsername(userDetails.getUsername());
//            if(staff.isPresent()) {
//                name = staff.get().getName();
//            }
//        }

        return new AuthenticationResponse(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getUsername(),
                userDetails.getName(),
                userDetails.getMemberSince(),
                roles);
    }
    }

