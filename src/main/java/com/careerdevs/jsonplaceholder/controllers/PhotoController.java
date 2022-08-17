package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.CommentModel;
import com.careerdevs.jsonplaceholder.models.PhotoModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/photos")
public class PhotoController {
    private final String jsonPlaceholderEndpointPhotos = "https://jsonplaceholder.typicode.com/photos";

    @GetMapping("/all")
    public ResponseEntity<?> getAllPhotos (RestTemplate restTemplate){
        try {
           PhotoModel[] response = restTemplate.getForObject(jsonPlaceholderEndpointPhotos, PhotoModel[].class);
            return ResponseEntity.ok(response);
        } catch( Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getPhotoById(RestTemplate restTemplate,  @PathVariable String id){
        try{
            Integer.parseInt(id);
            PhotoModel response = restTemplate.getForObject(jsonPlaceholderEndpointPhotos+"/"+id, PhotoModel.class);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e){
            return ResponseEntity.status(400).body("Invalid Id: " + id);
        }catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(400).body("Photo not Found With ID: " + id);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}
