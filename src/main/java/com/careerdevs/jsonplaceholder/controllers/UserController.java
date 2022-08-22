package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.UserModel;
import org.apache.catalina.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Source;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final String jsonPlaceholderEndpointUsers = "https://jsonplaceholder.typicode.com/users";

    @RequestMapping("/")
    public String route() {
        return "Hi";
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUsers(RestTemplate restTemplate) {
        try {
            UserModel[] response = restTemplate.getForObject(jsonPlaceholderEndpointUsers, UserModel[].class);


            for (int i = 0; i < response.length; i++) {
                UserModel user = response[i];
                System.out.println(user.getName());
                System.out.println(user.getCompany().getName());
            }


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(RestTemplate restTemplate, @PathVariable String id) {
        try {
            Integer.parseInt(id);
            System.out.println("Getting user with id: " + id);
            String url = jsonPlaceholderEndpointUsers + "/" + id;
            UserModel response = restTemplate.getForObject(url, UserModel.class);
            return ResponseEntity.ok(response);
            //when exception occurs in a try code block  it's going to go down the list of catch-statements
            //the first one that matches the exception  that just occured will run that catch block
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("User Not Found With ID: " + id);

            //this catch statement is very general and all errors will fall under this exception,
            //which is why more specific statements should be on top this one, which is more general
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }


    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteUserById (RestTemplate restTemplate, @PathVariable String id){
        try {
            Integer.parseInt(id);
            System.out.println("Getting user with id: " + id);
            String url = jsonPlaceholderEndpointUsers + "/" + id;
            restTemplate.getForObject(url, UserModel.class);
            restTemplate.delete(url);
            return ResponseEntity.ok("User with ID "+ id + " was deleted");
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

    //POST localhost:8080/api/users
    @PostMapping("/")
    ResponseEntity<?> createNewUser(RestTemplate restTemplate, @RequestBody UserModel newUser){
        try{

            //todo: data validation

            UserModel createdUser = restTemplate.postForObject(jsonPlaceholderEndpointUsers,newUser,UserModel.class);
            return ResponseEntity.ok(createdUser);
        }catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateUser(RestTemplate restTemplate, @RequestBody UserModel updateUserData, @PathVariable String id){
        try{
            Integer.parseInt(id);
            String url = jsonPlaceholderEndpointUsers + "/" + id;
            HttpEntity<UserModel> reqEntity = new HttpEntity<>(updateUserData);
            ResponseEntity<UserModel> jphRes = restTemplate.exchange(url, HttpMethod.PUT, reqEntity, UserModel.class);


            return ResponseEntity.ok(jphRes.getBody());

        }  catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("User Not Found With ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}


