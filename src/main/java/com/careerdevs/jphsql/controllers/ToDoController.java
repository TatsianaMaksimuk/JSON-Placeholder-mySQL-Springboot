package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.ToDoModel;
import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.ToDoRepository;
import com.careerdevs.jphsql.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3500")
public class ToDoController {
    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/todos";

    @Autowired
    private ToDoRepository toDoRepository;


    //GET
    //Getting all ToDos from jsonplaceholder API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllToDosAPI(RestTemplate restTemplate) {
        try {
            ToDoModel[] allToDos = restTemplate.getForObject(JPH_API_URL, ToDoModel[].class);
            return ResponseEntity.ok(allToDos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Getting ToDo by ID from jsonPlaceholder API
    @GetMapping("/jph/id/{id}")
    public ResponseEntity<?> getToDoByIdFromAPI(RestTemplate restTemplate, @PathVariable String id) {
        try {
            Integer.parseInt(id);
            String url = JPH_API_URL + "/" + id;
            ToDoModel response = restTemplate.getForObject(url, ToDoModel.class);
            return ResponseEntity.ok(response);
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

    //Getting all ToDos from local SQLDataBase
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllToDosFromSQL() {
        try {
            ArrayList<ToDoModel> allToDos = (ArrayList<ToDoModel>) toDoRepository.findAll();
            return ResponseEntity.ok(allToDos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Getting one To Do by ID from SQL database
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOneToDoByIdFromSQL(@PathVariable String id) {
        try {
            int toDoId = Integer.parseInt(id);
            Optional<ToDoModel> foundToDo = toDoRepository.findById(toDoId);
            if (foundToDo.isEmpty()) return ResponseEntity.status(404).body("To Do Not Found With ID: " + id);
            return ResponseEntity.ok(foundToDo.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    //POST
    //Posting all ToDos to sql database
    @PostMapping("/sql/all")
    public ResponseEntity<?> uploadAllToDosToSQL(RestTemplate restTemplate) {
        try {
            ToDoModel[] allToDos = restTemplate.getForObject(JPH_API_URL, ToDoModel[].class);

            for (ToDoModel allToDo : allToDos) {
                allToDo.removeID();
            }

            assert allToDos != null;
            toDoRepository.saveAll(Arrays.asList(allToDos));

            return ResponseEntity.ok(allToDos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Posting ONE To Do to sql database
    @PostMapping
    public ResponseEntity<?> uploadOneToDoToSQL(@RequestBody ToDoModel newToDoData) {
        try {
            newToDoData.removeID();
            ToDoModel savedToDo = toDoRepository.save(newToDoData);
            return ResponseEntity.ok(savedToDo);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //DELETE
    //deleting all ToDos from sql
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllToDosFromSQL() {
        try {
            long count = toDoRepository.count();
            toDoRepository.deleteAll();
            return ResponseEntity.ok("Deleted ToDos" + count);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


    //Delete one To Do from sql database
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteToDoByIdFromSQL(@PathVariable String id) {
        try {
            int toDoId = Integer.parseInt(id);
            Optional<ToDoModel> foundToDo = toDoRepository.findById(toDoId);

            if (foundToDo.isEmpty()) return ResponseEntity.status(404).body("To Do Not Found With ID: " + id);

            toDoRepository.deleteById(toDoId);

            return ResponseEntity.ok(foundToDo.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("To Do Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //PUT
    //Put one To Do by ID (SQL)-make sure a user with the given id already exists
    @PutMapping("/sql/id/{id}")
    public ResponseEntity<?> updateOneUserInSQL(@PathVariable String id, @RequestBody ToDoModel updateToDoData) {
        try {
            int toDoId = Integer.parseInt(id);
            Optional<ToDoModel> foundToDo = toDoRepository.findById(toDoId);
            if (foundToDo.isEmpty()) return ResponseEntity.status(404).body("To Do Not Found With ID: " + id);
            if (toDoId != updateToDoData.getId()) return ResponseEntity.status(400).body("To Do IDs dod not match");
            toDoRepository.save(updateToDoData);
            return ResponseEntity.ok(foundToDo.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}






