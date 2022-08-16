package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Source;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final String jsonPlaceholderEndpoint = "https://jsonplaceholder.typicode.com/users";

    @RequestMapping("/")
    public String route() {
        return "Hi";
    }

    @GetMapping("/all")
    public ResponseEntity<?> getUsers(RestTemplate restTemplate) {
        try {
            UserModel[] response = restTemplate.getForObject(jsonPlaceholderEndpoint, UserModel[].class);


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
            String url = jsonPlaceholderEndpoint + "/" + id;
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

    @GetMapping("/posts")
    public Object postsHandler(RestTemplate restTemplate) {
        return restTemplate.getForObject(jsonPlaceholderEndpoint + "/posts", Object.class);
    }

    @GetMapping("/photos")
    public Object photoHandler(RestTemplate restTemplate) {
        return restTemplate.getForObject(jsonPlaceholderEndpoint + "/photos", Object.class);

    }

    @GetMapping("/todos")
    public Object toDoHandler(RestTemplate restTemplate) {
        return restTemplate.getForObject(jsonPlaceholderEndpoint + "/todos", Object.class);
    }

}


