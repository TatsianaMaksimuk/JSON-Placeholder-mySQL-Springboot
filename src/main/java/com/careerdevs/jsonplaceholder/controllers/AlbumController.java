package com.careerdevs.jsonplaceholder.controllers;

import com.careerdevs.jsonplaceholder.models.AlbumModel;
import com.careerdevs.jsonplaceholder.models.PhotoModel;
import com.careerdevs.jsonplaceholder.models.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteAlbumById (RestTemplate restTemplate, @PathVariable String id){
        try {
            Integer.parseInt(id);
            String url = jsonPlaceholderEndpointAlbums + "/" + id;
            restTemplate.getForObject(url, AlbumModel.class);
            restTemplate.delete(url);
            return ResponseEntity.ok("Album with ID "+ id + " was deleted");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Album Not Found With ID: " + id);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
