package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.AlbumModel;
import com.careerdevs.jsonplaceholder.models.PhotoModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("api/albums")
public class AlbumController {
    private final String jsonPlaceholderEndpointAlbums = "https://jsonplaceholder.typicode.com/albums";

    @GetMapping("/all")
    public ResponseEntity<?> getAllAlbums (RestTemplate restTemplate){
        try {
            AlbumModel[] response = restTemplate.getForObject(jsonPlaceholderEndpointAlbums, AlbumModel[].class);
            return ResponseEntity.ok(response);
        } catch( Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAlbumById(RestTemplate restTemplate,  @PathVariable String id){
        try{
            Integer.parseInt(id);
           AlbumModel response = restTemplate.getForObject(jsonPlaceholderEndpointAlbums+"/"+id, AlbumModel.class);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e){
            return ResponseEntity.status(400).body("Invalid Id: " + id);
        }catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(400).body("Album not Found With ID: " + id);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
