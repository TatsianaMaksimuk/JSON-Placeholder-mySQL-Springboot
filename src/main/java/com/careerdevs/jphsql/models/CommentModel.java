package com.careerdevs.jphsql.models;

import javax.persistence.*;

@Entity
@Table(name="Comment")
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int postId;
    private String name;

    @Column(length = 512)//default length is 255. Column - each field in sql db
    private String body;
    private String email;



    public int getId() {
        return id;
    }

    public void removeID(){
        id = 0;
    }

    public int getPostId() {
        return postId;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getEmail() {
        return email;
    }


}
