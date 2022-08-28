package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.CommentModel;
import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/comments";
    @Autowired
    private CommentRepository commentRepository;


    //GET
    //Getting all comments from jph API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllCommentsAPI(RestTemplate restTemplate) {
        try {
            CommentModel[] response = restTemplate.getForObject(JPH_API_URL, CommentModel[].class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    //Getting allComments from SQL
    @RequestMapping("/sql/all")
    public ResponseEntity<?> getAllCommentsFromSQL() {
        try {
            ArrayList<CommentModel> allComments = (ArrayList<CommentModel>) commentRepository.findAll();
            return ResponseEntity.ok(allComments);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // finding max body length for comment to make characters limit in db higher:
    @GetMapping("/findBodyMax")
    public ResponseEntity<?> findBodyMax(RestTemplate restTemplate) {
        try {

            CommentModel[] allComments = restTemplate.getForObject(JPH_API_URL, CommentModel[].class);
            assert allComments != null;

            int maxLen = 0;
            for (CommentModel comment : allComments
            ) {
                if (comment.getBody().length() > maxLen) {
                    maxLen = comment.getBody().length();
                }
            }


            return ResponseEntity.ok("Max Length Body:" + maxLen);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    //Getting one comment by ID from jph API
    @GetMapping("/jph/id/{id}")
    public ResponseEntity<?> getCommentByIdFromAPI(RestTemplate restTemplate, @PathVariable String id) {
        try {
            Integer.parseInt(id);
            CommentModel response = restTemplate.getForObject(JPH_API_URL + "/" + id, CommentModel.class);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid Id: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(400).body("Comment not Found With ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @PostMapping("/all")
    public ResponseEntity<?> uploadAllCommentsToSQL(RestTemplate restTemplate) {
        try {
            CommentModel[] allComments = restTemplate.getForObject(JPH_API_URL, CommentModel[].class);

            assert allComments != null;

            for (CommentModel comment : allComments) {
                comment.removeID();
            }

            commentRepository.saveAll(Arrays.asList(allComments));


            return ResponseEntity.ok(allComments);
        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping()
    public ResponseEntity<?> uploadOneCommentToSQL(@RequestBody CommentModel newCommentData) {
        try {
            newCommentData.removeID();
            CommentModel savedComment = commentRepository.save(newCommentData);
            return ResponseEntity.ok(savedComment);

        } catch (Exception e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}

