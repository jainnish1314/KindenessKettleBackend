package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.DTO.CommentRequestDTO;
import com.kindnesskattle.bddAtcProject.Entities.Comment;
import com.kindnesskattle.bddAtcProject.Services.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@RequestBody CommentRequestDTO commentRequest) {
        Comment comment = commentService.addComment(commentRequest.getUserId(), commentRequest.getPostId(), commentRequest.getCommentContent());
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/getComment/{id}")
    public List<Map<String,Object>> getCommentById(@PathVariable Long id)
    {
        return commentService.getComment(id);
    }

    @DeleteMapping("/delete_comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
        try {
            boolean deleted = commentService.deleteComment(commentId);
            if (deleted) {
                return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Comment not found or could not be deleted", HttpStatus.NOT_FOUND);
            }
        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>("Comment not found", HttpStatus.NOT_FOUND);
            throw new EntityNotFoundException(e.getMessage());
        } catch (RuntimeException e) {
//            return new ResponseEntity<>("Error deleting comment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e.getMessage());
        }
    }
}
