package com.example.trello.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user", indexes = @Index(name = "userIndex", columnList = "user_name, user_email, user_password"), schema = "public")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private long user_id;

    @Column(name = "user_name")
    @NotBlank
    private String userName;

    @Column(name = "user_email")
    @NotBlank
    private String userEmail;

    @Column(name = "user_password")
    @NotBlank
    private String userPassword;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();



    public User(long user_id, String userName, String userEmail, String userPassword) {
        this.user_id = user_id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public User(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;

    }
}
