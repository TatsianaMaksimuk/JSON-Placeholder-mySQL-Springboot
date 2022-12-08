package com.careerdevs.jphsql.models;

import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;

@Entity
@Table(name="Album")
@CrossOrigin(origins = "http://localhost:3500")
public class AlbumModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private int userId;

    public int getId() {
        return id;
    }
    public void removeID(){
        id = 0;
    }

    public void removeId() {
        id = 0;
    }

    public String getTitle() {
        return title;
    }

    public int getUserId() {
        return userId;
    }


}
