package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name="Post")
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
