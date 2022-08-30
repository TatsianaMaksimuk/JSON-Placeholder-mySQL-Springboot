package com.careerdevs.jphsql.controllers;

import com.careerdevs.jphsql.models.CommentModel;
import com.careerdevs.jphsql.models.UserModel;
import com.careerdevs.jphsql.repositories.CommentRepository;
import com.careerdevs.jphsql.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final String JPH_API_URL = "https://jsonplaceholder.typicode.com/comments";
    @Autowired
    private CommentRepository commentRepository;


    //GET
    //
    //Getting all comments from jph API
    @GetMapping("/jph/all")
    public ResponseEntity<?> getAllCommentsFromAPI(RestTemplate restTemplate) {
        try {
            CommentModel[] response = restTemplate.getForObject(JPH_API_URL, CommentModel[].class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
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
            return ResponseEntity.status(400).body("Invalid ID: " + id);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(400).body("Comment not Found With ID: " + id);
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
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @RequestMapping("/sql/id/{id}")
    public ResponseEntity<?> getOneCommentFromSQL(@PathVariable String id) {
        try {
            int commentID = Integer.parseInt(id);
            Optional<CommentModel> foundComment = commentRepository.findById(commentID);
            if (foundComment.isEmpty()) return ResponseEntity.status(404).body("Comment Not Found With ID: " + id);
            return ResponseEntity.ok(foundComment.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
//Getting comment by ID from SQL database


    //POST
    //
    //Posting all comments to sql database
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

    //Posting ONE comment to sql database
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

    //DELETE
    //
    //Deleting all comments from sql
    @DeleteMapping("/sql/all")
    public ResponseEntity<?> deleteAllCommentsFromSQL(RestTemplate restTemplate) {
        try {
            long count = commentRepository.count();
            commentRepository.deleteAll();
            return ResponseEntity.ok("Deleted comments:" + count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    //Delete one comment from sql database
    @DeleteMapping("/sql/id/{id}")
    public ResponseEntity<?> deleteOneCommentByIdFromSQL(@PathVariable String id) {
        try {
            int commentId = Integer.parseInt(id);
            Optional<CommentModel> foundComment = commentRepository.findById(commentId);
            if (foundComment.isEmpty())
                return ResponseEntity.status(404).body("Comment Not Found with Id: " + commentId);
            commentRepository.deleteById(commentId);
            return ResponseEntity.ok(foundComment.get());
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("ID: " + id + ", is not a valid id. Must be a whole number");
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(400).body("Comment Not Found With ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}

