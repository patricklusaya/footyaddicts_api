package com.example.footyaddicts.service;

import com.example.footyaddicts.models.ERole;
import com.example.footyaddicts.repos.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;

    public Long findSumOfStaffByRole(ERole role) {
        return staffRepository.findSumOfStaffByRole(role);
    }
}
