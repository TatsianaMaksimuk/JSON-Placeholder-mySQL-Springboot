package com.careerdevs.jphsql.models;

import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;

@Entity
@Table(name="User")
@CrossOrigin(origins = "http://localhost:3500")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;



    public int getId() {
        return id;
    }

    public void removeID(){
        id = 0;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }



}
