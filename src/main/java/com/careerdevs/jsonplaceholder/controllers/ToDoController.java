package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.ToDoModel;
import com.careerdevs.jsonplaceholder.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteToDoById (RestTemplate restTemplate, @PathVariable String id){
        try {
            Integer.parseInt(id);
            String url = jsonPlaceholderEndpointToDos + "/" + id;
            restTemplate.getForObject(url, ToDoModel.class);
            restTemplate.delete(url);
            return ResponseEntity.ok("To Do with ID "+ id + " was deleted");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("To Do Not Found With ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping("/")
    ResponseEntity<?> createNewToDo(RestTemplate restTemplate, @RequestBody ToDoModel newToDo){
        try{
            //todo: data validation
            ToDoModel createdToDo = restTemplate.postForObject(jsonPlaceholderEndpointToDos,newToDo,ToDoModel.class);
            return ResponseEntity.ok(createdToDo);
        }catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
