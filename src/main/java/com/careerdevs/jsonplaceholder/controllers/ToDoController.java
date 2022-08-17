package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.ToDoModel;
import com.careerdevs.jsonplaceholder.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {
    private final String jsonPlaceholderEndpointToDos = "https://jsonplaceholder.typicode.com/posts";

    @GetMapping("/all")
    public ResponseEntity<?> getAllToDos(RestTemplate restTemplate) {
        try {
            ToDoModel[] response = restTemplate.getForObject(jsonPlaceholderEndpointToDos, ToDoModel[].class);
            return ResponseEntity.ok(response);
        } catch( Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getToDoById(RestTemplate restTemplate,  @PathVariable String id){
        try{
            Integer.parseInt(id);
            ToDoModel response = restTemplate.getForObject(jsonPlaceholderEndpointToDos+"/"+id, ToDoModel.class);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e){
            return ResponseEntity.status(400).body("Invalid Id: " + id);
        }catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(400).body("ToDo not Found With ID: " + id);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
