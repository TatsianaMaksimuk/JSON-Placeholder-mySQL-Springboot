package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.PostModel;
import com.careerdevs.jsonplaceholder.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
