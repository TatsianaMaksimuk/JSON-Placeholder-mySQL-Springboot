package com.careerdevs.jphsql.models;

import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;

@Entity
@Table(name="Post")
@CrossOrigin(origins = "http://localhost:3500")
public class PostModel {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userId;
    private String title;
    private String body;



    public int getId() {
        return id;
    }

    public void removeID(){
        id = 0;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
