package com.careerdevs.jphsql.models;

import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;

@Entity
@Table(name="Photo")
@CrossOrigin(origins = "http://localhost:3500")
public class PhotoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int albumId;
    private String title;
    private String url;


    public int getId() {
        return id;
    }

    public void removeId(){
        id = 0;
    }

    public int getAlbumId() {
        return albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

}
