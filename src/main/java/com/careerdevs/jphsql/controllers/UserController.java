package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/users";

    @Autowired
    private UserRepository userRepository;



    //GET
    //Getting all users from jsonplaceholder API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllUsersAPI(RestTemplate restTemplate) {
        try {
            UserModel[] allUsers = restTemplate.getForObject(JPH_API_URL, UserModel[].class);
            return ResponseEntity.ok(allUsers);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Getting user by ID from jsonPlaceholder API
    @GetMapping("/jph/id/{id}")
    public ResponseEntity<?> getUserByIdFromAPI(RestTemplate restTemplate, @PathVariable String id) {
        try {
            Integer.parseInt(id);
            String url = JPH_API_URL + "/" + id;
            UserModel response = restTemplate.getForObject(url, UserModel.class);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("User Not Found With ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Getting all users from local SQLDataBase
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllUsersFromSQL() {
        try {
            ArrayList<UserModel> allUsers = (ArrayList<UserModel>) userRepository.findAll();
            return ResponseEntity.ok(allUsers);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Getting user by ID from DQL database
    @GetMapping("/sql/{id}")
    public ResponseEntity<?> getOneUserByIdFromSQL(@PathVariable String id) {
        try {

            // throws NumberFormatException if id is not a int
            int userId = Integer.parseInt(id);
            Optional<UserModel> foundUser = userRepository.findById(userId);
            if (foundUser.isEmpty()) return ResponseEntity.status(404).body("User Not Found With ID: " + id);
            //if (foundUser.isEmpty()) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);


            return ResponseEntity.ok(foundUser.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("User Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }




    //POST
    //Posting all users to sql database
    @PostMapping("/all")
    public ResponseEntity<?> uploadAllUsersToSQL(RestTemplate restTemplate) {
        try {
            UserModel[] allUsers = restTemplate.getForObject(JPH_API_URL, UserModel[].class);


//removes ID from each users
            for (UserModel allUser : allUsers) {
                allUser.removeID();
            }

//            for (int i = 0; i < allUsers.length; i++) {
//                allUsers[i].removeID();
//            }

            assert allUsers != null;

            //saves users to database and updates each User's ID field to the saved database ID
            userRepository.saveAll(Arrays.asList(allUsers));


            return ResponseEntity.ok(allUsers);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Posting ONE user to sql database
    @PostMapping
    public ResponseEntity<?> uploadOneUserToSQL(@RequestBody UserModel newUserData) {
        try {
            newUserData.removeID();
            UserModel savedUser = userRepository.save(newUserData);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //DELETE
    //deleting all users from sql
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllUsersFromSQL(RestTemplate restTemplate) {
        try {
            long count = userRepository.count();
            userRepository.deleteAll();
            return ResponseEntity.ok("Deleted Users" + count);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


    //Delete one user from sql database
    @DeleteMapping("/sql/{id}")
    public ResponseEntity<?> deleteUserByIdFromSQL(@PathVariable String id) {
        try {

            // throws NumberFormatException if id is not a int
            int userId = Integer.parseInt(id);

            Optional<UserModel> foundUser = userRepository.findById(userId);

            if (foundUser.isEmpty()) return ResponseEntity.status(404).body("User Not Found With ID: " + id);
            //if (foundUser.isEmpty()) throw new HttpClientErrorException(HttpStatus.NOT_FOUND);

            userRepository.deleteById(userId);


            return ResponseEntity.ok(foundUser.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("User Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}





