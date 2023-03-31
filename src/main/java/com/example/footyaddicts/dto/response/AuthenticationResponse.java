package com.example.footyaddicts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class AuthenticationResponse {
    private Long id;
    private String username;
    private String email;
    private String token;
    private  String name;
    private  String memberSince;
    private String type ="Bearer";
    private List<String> roles;


    public AuthenticationResponse(String jwt, Long id, String email, String username,String name ,String memberSince,  List<String> roles) {

        this.token = jwt;
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
        this.memberSince  = memberSince;
        this.roles = roles;
    }



}
