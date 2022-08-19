package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.PostModel;
import com.careerdevs.jsonplaceholder.models.ToDoModel;
import com.careerdevs.jsonplaceholder.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/posts")
public class PostController {
    private final String jsonPlaceholderEndpointPosts = "https://jsonplaceholder.typicode.com/posts";

    @GetMapping("all")
    public ResponseEntity<?> getAllPosts(RestTemplate restTemplate) {
        try {
            PostModel[] response = restTemplate.getForObject(jsonPlaceholderEndpointPosts, PostModel[].class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<?> getPostById(RestTemplate restTemplate, @PathVariable String id) {
        try{
            Integer.parseInt(id);
            String url = jsonPlaceholderEndpointPosts + "/" + id;
            PostModel response = restTemplate.getForObject(url, PostModel.class);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Post Not Found With ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deletePostById (RestTemplate restTemplate, @PathVariable String id){
        try {
            Integer.parseInt(id);
            String url = jsonPlaceholderEndpointPosts + "/" + id;
            restTemplate.getForObject(url, PostModel.class);
            restTemplate.delete(url);
            return ResponseEntity.ok("Post with ID "+ id + " was deleted");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Post Not Found With ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping("/")
    ResponseEntity<?> createNewPost(RestTemplate restTemplate, @RequestBody PostModel newPost){
        try{
            //todo: data validation
            PostModel createdPost = restTemplate.postForObject(jsonPlaceholderEndpointPosts,newPost,PostModel.class);
            return ResponseEntity.ok(createdPost);
        }catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
