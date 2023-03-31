package com.example.footyaddicts.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

import java.util.Set;
@AllArgsConstructor

@Getter
@Setter
public class RegistrationRequest {
    public  String username;
    public  String name;
    public  String email;
    public String password;
    private Set<String> role;
}
