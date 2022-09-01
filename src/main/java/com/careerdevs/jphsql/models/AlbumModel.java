package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name="Album")
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
