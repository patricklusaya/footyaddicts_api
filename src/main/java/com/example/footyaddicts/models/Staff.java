package com.example.footyaddicts.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(	name = "staff",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class Staff {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public Long id;
    @NotBlank
    public String username;
    @NotBlank
    public  String email;
    @NotBlank
    public  String memberSince;
    @NotBlank
    public String password;
    @NotBlank
    public String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "staff_roles",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> roles = new HashSet<>();
}
