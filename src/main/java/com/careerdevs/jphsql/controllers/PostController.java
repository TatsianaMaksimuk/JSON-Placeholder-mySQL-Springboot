package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.PostModel;
import com.careerdevs.jphsql.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/posts";


    @Autowired
    private PostRepository postRepository;


    //GET
    //Getting all posts from JPH API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllPostsFromAPI(RestTemplate restTemplate) {
        try {
            PostModel[] response = restTemplate.getForObject(JPH_API_URL, PostModel[].class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //getting post by ID from JPH API
    @GetMapping("/jph/id/{id}")
    public ResponseEntity<?> getPostByIdFromAPI(RestTemplate restTemplate, @PathVariable String id) {
        try {
            Integer.parseInt(id);
            PostModel response = restTemplate.getForObject(JPH_API_URL + '/'+id, PostModel.class);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid ID: " + id);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(400).body("Post not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //Getting all posts from SQL
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllPostsFromSQL() {
        try {
            ArrayList<PostModel> allPosts = (ArrayList<PostModel>) postRepository.findAll(); //explicit conversion () from List to an array list
            return ResponseEntity.ok(allPosts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //Getting post by ID from SQL database
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOnePostByIdFromSQL(@PathVariable String id) {
        try {
            int postId = Integer.parseInt(id);
            Optional<PostModel> foundPost = postRepository.findById(postId);
            if (foundPost.isEmpty()) return ResponseEntity.status(404).body("Post Not found With ID: "+ id);
            return ResponseEntity.ok(foundPost.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //POST
    //
    //Posting all posts to sql database
    @PostMapping("/all")
    public ResponseEntity<?> uploadAllPostsToSQL(RestTemplate restTemplate) {
        try {
            //1)get all Post from API
            PostModel[] allPosts = restTemplate.getForObject(JPH_API_URL, PostModel[].class);
            //2)remove ids from all posts
            for (int i = 0; i < allPosts.length; i++) {
                allPosts[i].removeID();
            }
            //save all posts to DB
            assert allPosts != null;

            postRepository.saveAll(Arrays.asList(allPosts));
            return ResponseEntity.ok(allPosts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //Posting ONE post
    //
    @PostMapping
    public ResponseEntity<?> uploadOnePostToSQL(@RequestBody PostModel newPostData){
        try{
            newPostData.removeID();
            PostModel savedPost = postRepository.save(newPostData);
            return ResponseEntity.ok(savedPost);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    //DELETE
    //
    //Deleting all posts from sql
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllPostsFromSQL (RestTemplate restTemplate){
        try {
            long count = postRepository.count();
            postRepository.deleteAll();
            return ResponseEntity.ok("Posts deleted: " + count);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    ////Delete one post from sql database
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOnePostByIdFromSQL (@PathVariable String id){
        try{
            int postId = Integer.parseInt(id);
            Optional<PostModel> foundPost = postRepository.findById(postId);
            if (foundPost.isEmpty()) return ResponseEntity.status(404).body("Post Not Found With ID: " + id);
            postRepository.deleteById(postId);
            return ResponseEntity.ok(foundPost.get());
        }catch (NumberFormatException e){
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}
