package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.AlbumModel;
import com.careerdevs.jphsql.models.CommentModel;
import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/albums";

    @Autowired
    private AlbumRepository albumRepository;

    //GET
    //Getting all albums from jsonPlaceholder API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllAlbumsAPI(RestTemplate restTemplate) {
        try {
            AlbumModel[] allAlbums = restTemplate.getForObject(JPH_API_URL, AlbumModel[].class);
            return ResponseEntity.ok(allAlbums);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //Getting one album by ID from jsonPlaceholder API
    @GetMapping("/jph/id/{id}")
    public ResponseEntity<?> getOneAlbumByIdFromAPI(RestTemplate restTemplate, @PathVariable String id) {
        try {
            int albumId = Integer.parseInt(id);
            String url = JPH_API_URL + "/" + albumId;
            AlbumModel response = restTemplate.getForObject(url, AlbumModel.class);
            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid id: " + id);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(404).body("Album Not Found With ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //Getting all albums from local SQLDataBase
    @GetMapping("/sql/all")
    public ResponseEntity<?> getAllAlbumsFromSQL() {
        try {
            ArrayList<AlbumModel> allAlbums = (ArrayList<AlbumModel>) albumRepository.findAll();
            return ResponseEntity.ok(allAlbums);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


    //Getting one album by ID from SQL database
    @GetMapping("/sql/id/{id}")
    public ResponseEntity<?> getOneAlbumByIdFromSQL(@PathVariable String id) {
        try {

            int albumId = Integer.parseInt(id);
            Optional<AlbumModel> foundAlbum = albumRepository.findById(albumId);
            if (foundAlbum.isEmpty()) return ResponseEntity.status(404).body("Album Not Found With ID: " + id);
            return ResponseEntity.ok(foundAlbum.get());

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    //POST
    //Posting all albums to sql database
    @PostMapping("/all")
    public ResponseEntity<?> uploadAllAlbumsToSQL(RestTemplate restTemplate) {
        try {
            AlbumModel[] allAlbums = restTemplate.getForObject(JPH_API_URL, AlbumModel[].class);


            for (AlbumModel allAlbum : allAlbums) {
                allAlbum.removeID();
            }

            albumRepository.saveAll(Arrays.asList(allAlbums));


            return ResponseEntity.ok(allAlbums);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Posting ONE album to sql database
    @PostMapping
    public ResponseEntity<?> uploadOneAlbumToSQL (@RequestBody AlbumModel newAlbumData){
        try{
            newAlbumData.removeId();
            AlbumModel savedAlbum = albumRepository.save(newAlbumData);
            return ResponseEntity.ok(savedAlbum);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //DELETE
    //
    //Deleting all albums from sql
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllAlbumsFromSQL (){
        try {
            long count = albumRepository.count();
            albumRepository.deleteAll();
            return ResponseEntity.ok("Deleted Albums: " + count);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //Delete one album from sql database

    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOneAlbumByIdFromSQL(@PathVariable String id) {
        try {
            int albumId = Integer.parseInt(id);
            Optional<AlbumModel> foundAlbum = albumRepository.findById(albumId);
            if (foundAlbum.isEmpty())
                return ResponseEntity.status(404).body("Album Not Found with Id: " + albumId);
            albumRepository.deleteById(albumId);
            return ResponseEntity.ok(foundAlbum.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(400).body("Comment Not Found With ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    //PUT
    //Put one album by ID (SQL)-make sure a user with the given id already exists

    @PutMapping("/sql/id/{id}")
    public ResponseEntity<?> updateOneAlbumInSQL(@PathVariable String id, @RequestBody AlbumModel updateAlbumData) {
        try {
            int albumId = Integer.parseInt(id);
            Optional<AlbumModel> foundAlbum = albumRepository.findById(albumId);
            if (foundAlbum.isEmpty()) return ResponseEntity.status(404).body("Album Not Found With ID: " + id);
            if (albumId != updateAlbumData.getId()) return ResponseEntity.status(400).body("Album IDs dod not match");
            albumRepository.save(updateAlbumData);
            return ResponseEntity.ok(foundAlbum.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
