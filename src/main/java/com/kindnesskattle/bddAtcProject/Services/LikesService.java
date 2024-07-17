package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.DTO.LikesSummaryDTO;
import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.Likes;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.DonationPostRepository;
import com.kindnesskattle.bddAtcProject.Repository.LikesRepository;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LikesService {

    private final LikesRepository likesRepository;
    private final UserAccountRepository userAccountRepository;
    private final DonationPostRepository donationPostRepository;

    @Autowired
    public LikesService(LikesRepository likesRepository, UserAccountRepository userAccountRepository, DonationPostRepository donationPostRepository) {
        this.likesRepository = likesRepository;
        this.userAccountRepository = userAccountRepository;
        this.donationPostRepository = donationPostRepository;
    }

    public void addLike(Long userId, Long postId) {
        if (userId == null || postId == null) {
            log.warn("User ID or Post ID is null. Skipping like operation.");
            return;
        }

        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DonationPost post = donationPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Donation post not found"));

        if (user.getUsername() == null || post.getPostId() == null) {
            log.warn("Username or post ID is null. Skipping like operation.");
            return;
        }

        log.info("Log message: username = " + user.getUsername() + ", post id = " + post.getPostId());

        Optional<Likes> existingLike = likesRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            throw new RuntimeException("User has already liked this post.");
        }

        Likes like = new Likes();
        like.setUser(user);
        like.setPost(post);
        like.setLikeDateTime(LocalDateTime.now());

        likesRepository.save(like);
    }

    public List<LikesSummaryDTO> getLikesSummaryByPostId(Long postId) {
        List<Likes> likesList = likesRepository.findAllByPost_PostId(postId);

        return likesList.stream()
                .map(this::createLikesSummaryDTO)
                .collect(Collectors.toList());
    }

    private LikesSummaryDTO createLikesSummaryDTO(Likes likes) {
        Long postId = likes.getPost().getPostId();
        return new LikesSummaryDTO(likes.getUser(), postId);
    }

    public void deleteLike(Long userId, Long postId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DonationPost post = donationPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Likes likeToDelete = likesRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        likesRepository.delete(likeToDelete);
    }
}
