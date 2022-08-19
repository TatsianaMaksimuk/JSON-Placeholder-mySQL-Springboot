package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.CommentModel;
import com.careerdevs.jsonplaceholder.models.ToDoModel;
import com.careerdevs.jsonplaceholder.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/comments")

public class CommentController {

    private final String jsonPlaceholderEndpointComments = "https://jsonplaceholder.typicode.com/comments";

    @GetMapping("/all")
    public ResponseEntity<?> getAllComments (RestTemplate restTemplate){
        try {
            CommentModel[] response = restTemplate.getForObject(jsonPlaceholderEndpointComments, CommentModel[].class);
            return ResponseEntity.ok(response);
        } catch( Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> geCommentById(RestTemplate restTemplate,  @PathVariable String id){
        try{
            Integer.parseInt(id);
           CommentModel response = restTemplate.getForObject(jsonPlaceholderEndpointComments+"/"+id, CommentModel.class);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e){
            return ResponseEntity.status(400).body("Invalid Id: " + id);
        }catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(400).body("Comment not Found With ID: " + id);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteCommentById (RestTemplate restTemplate, @PathVariable String id){
        try {
            Integer.parseInt(id);
            String url = jsonPlaceholderEndpointComments + "/" + id;
            restTemplate.getForObject(url, CommentModel.class);
            restTemplate.delete(url);
            return ResponseEntity.ok("Comment with ID "+ id + " was deleted");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Comment Not Found With ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
