package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.PostModel;
import com.careerdevs.jphsql.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/posts";


    @Autowired
    private PostRepository postRepository;


    //GET
    //Getting all posts from JPH API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllPostsFromAPI(RestTemplate restTemplate){
      try  {
            PostModel[] response = restTemplate.getForObject(JPH_API_URL, PostModel[].class);
            return ResponseEntity.ok(response);
        } catch (Exception e){
          return ResponseEntity.internalServerError().body(e.getMessage());
      }
    }

    //getting post by ID from JPH API
    @GetMapping("/jph/id/{id}")
    public ResponseEntity<?> getPostByIdFromAPI(RestTemplate restTemplate, @PathVariable String id){
        try {
            Integer.parseInt(id);
            PostModel response = restTemplate.getForObject(JPH_API_URL+id, PostModel.class);
            return ResponseEntity.ok(response);
        }catch (NumberFormatException e){
            return ResponseEntity.status(400).body("Invalid ID: " + id);
        }catch (HttpClientErrorException e){
            return ResponseEntity.status(400).body("Post not found with ID: "+id);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //Getting all posts from SQL
    //Getting user by ID from SQL database

    //POST
    //
    //Posting all posts to sql database
    //Posting ONE post
    //
    //DELETE
    //
    //Deleting all posts from sql
    ////Delete one post from sql database


}
