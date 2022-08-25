package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/users";

    @Autowired
    private UserRepository userRepository;

    //getting users from jsonplaceholder API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllUsersAPI (RestTemplate restTemplate) {
        try {
            UserModel[] allUsers = restTemplate.getForObject(JPH_API_URL, UserModel[].class);
            return ResponseEntity.ok(allUsers);
        }catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //getting users from local SQLDataBase
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllUsersSQL () {
        try {
            ArrayList<UserModel> allUsers= (ArrayList<UserModel>) userRepository.findAll();

            return ResponseEntity.ok(allUsers);
        }catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


    @PostMapping("/all")
    public ResponseEntity<?> uploadAllUsersToSQL (RestTemplate restTemplate) {
        try {
            UserModel[] allUsers = restTemplate.getForObject(JPH_API_URL, UserModel[].class);

            assert allUsers != null;
            List<UserModel> savedUsers = userRepository.saveAll(Arrays.asList(allUsers));


            return ResponseEntity.ok(allUsers);
        }catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping
    public ResponseEntity<?> uploadOneUser (@RequestBody UserModel newUserData) {
        try {
            UserModel savedUser =  userRepository.save(newUserData);
            return ResponseEntity.ok(savedUser);
        }catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


}
