package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.PhotoModel;
import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:3500")
public class PhotoController {

    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/photos";

    @Autowired
    private PhotoRepository photoRepository;

    //GET
    //Getting all Photos from jsonplaceholder API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllPhotosAPI(RestTemplate restTemplate) {
        try {
            PhotoModel[] allPhotos = restTemplate.getForObject(JPH_API_URL, PhotoModel[].class);
            return ResponseEntity.ok(allPhotos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Getting Photo by ID from jsonPlaceholder API
    @GetMapping("/jph/id/{id}")
    public ResponseEntity<?> getPhotoByIdFromAPI(RestTemplate restTemplate, @PathVariable String id) {
        try {
            Integer.parseInt(id);
            String url = JPH_API_URL + "/" + id;
            PhotoModel response = restTemplate.getForObject(url, PhotoModel.class);
            return ResponseEntity.ok(response);
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

    //Getting all Photos from local SQLDataBase
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllPhotosFromSQL() {
        try {
            ArrayList<PhotoModel> allPhotos = (ArrayList<PhotoModel>) photoRepository.findAll();
            return ResponseEntity.ok(allPhotos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Getting one Photo by ID from SQL database
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOnePhotoByIdFromSQL(@PathVariable String id) {
        try {
            int photoId = Integer.parseInt(id);
            Optional<PhotoModel> foundPhoto = photoRepository.findById(photoId);
            if (foundPhoto.isEmpty()) return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);

            return ResponseEntity.ok(foundPhoto.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    //POST
    //Posting all Photos to sql database
    @PostMapping("/sql/all")
    public ResponseEntity<?> uploadAllPhotosToSQL(RestTemplate restTemplate) {
        try {
            PhotoModel[] allPhotos = restTemplate.getForObject(JPH_API_URL, PhotoModel[].class);
            for (PhotoModel allPhoto : allPhotos) {
                allPhoto.removeId();
            }
            assert allPhotos != null;
            photoRepository.saveAll(Arrays.asList(allPhotos));
            return ResponseEntity.ok(allPhotos);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Posting ONE Photo to sql database
    @PostMapping
    public ResponseEntity<?> uploadOnePhotoToSQL(@RequestBody PhotoModel newPhotoData) {
        try {
            newPhotoData.removeId();
            PhotoModel savedPhoto = photoRepository.save(newPhotoData);
            return ResponseEntity.ok(savedPhoto);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //DELETE
    //deleting all Photos from sql
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllPhotosFromSQL() {
        try {
            long count = photoRepository.count();
            photoRepository.deleteAll();
            return ResponseEntity.ok("Deleted Photos" + count);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


    //Delete one user from sql database
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deletePhotoByIdFromSQL(@PathVariable String id) {
        try {

            int photoId = Integer.parseInt(id);

            Optional<PhotoModel> foundPhoto = photoRepository.findById(photoId);

            if (foundPhoto.isEmpty()) return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);

            photoRepository.deleteById(photoId);

            return ResponseEntity.ok(foundPhoto.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //PUT
    //Put one Photo by ID (SQL)-make sure a user with the given id already exists
    @PutMapping("/sql/id/{id}")
    public ResponseEntity<?> updateOnePhotoInSQL(@PathVariable String id, @RequestBody PhotoModel updatePhotoData) {
        try {
            int photoId = Integer.parseInt(id);
            Optional<PhotoModel> foundPhoto = photoRepository.findById(photoId);
            if (foundPhoto.isEmpty()) return ResponseEntity.status(404).body("Photo Not Found With ID: " + id);
            if (photoId != updatePhotoData.getId()) return ResponseEntity.status(400).body("Photo IDs dod not match");
            photoRepository.save(updatePhotoData);
            return ResponseEntity.ok(foundPhoto.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }



}
