package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.CommentModel;
import com.careerdevs.jsonplaceholder.models.PhotoModel;
import com.careerdevs.jsonplaceholder.models.PostModel;
import com.careerdevs.jsonplaceholder.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deletePhotoById (RestTemplate restTemplate, @PathVariable String id){
        try {
            Integer.parseInt(id);
            String url = jsonPlaceholderEndpointPhotos + "/" + id;
            restTemplate.getForObject(url, PhotoModel.class);
            restTemplate.delete(url);
            return ResponseEntity.ok("Photo with ID "+ id + " was deleted");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
    @PostMapping("/")
    ResponseEntity<?> createNewPhoto(RestTemplate restTemplate, @RequestBody PhotoModel newPhoto){
        try{
            //todo: data validation
            PhotoModel createdPhoto = restTemplate.postForObject(jsonPlaceholderEndpointPhotos,newPhoto,PhotoModel.class);
            return ResponseEntity.ok(createdPhoto);
        }catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
