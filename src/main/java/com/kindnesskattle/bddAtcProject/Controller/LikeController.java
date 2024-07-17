package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.DTO.LikesSummaryDTO;
import com.kindnesskattle.bddAtcProject.Services.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/kindnessKettle/like")
@Slf4j
public class LikeController {

    @Autowired
    private final LikesService likesService;

    public LikeController(LikesService likesService) {
        this.likesService = likesService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addLike(@RequestParam Long userId, @RequestParam Long postId) {
        try {
            log.info("Log message :- userID= "+userId +"PostID = "+ postId);
            likesService.addLike(userId, postId);
            return ResponseEntity.ok("Like added successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<LikesSummaryDTO>> getLikes(@RequestParam Long postId) {
        try {
            log.info("Log message :- PostID = " + postId);

            // Call LikesService to retrieve likes information
            List<LikesSummaryDTO> likesSummaryList = likesService.getLikesSummaryByPostId(postId);

            Long totalLikes = Long.valueOf(likesSummaryList.size());

            likesSummaryList.forEach(dto -> dto.setTotalLikes(totalLikes));
            return ResponseEntity.ok(likesSummaryList);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteLike(@RequestParam Long userId, @RequestParam Long postId) {
        likesService.deleteLike(userId, postId);
        return ResponseEntity.ok("Like deleted successfully.");
    }
}
