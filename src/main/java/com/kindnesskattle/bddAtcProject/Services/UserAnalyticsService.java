package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.DTO.UserAnalyticsDTO;
import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.Likes;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.DonationPostRepository;
import com.kindnesskattle.bddAtcProject.Repository.LikesRepository;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAnalyticsService {

    private final UserAccountRepository userAccountRepository;
    private final DonationPostRepository donationPostRepository;
    private final LikesRepository likesRepository;

    @Autowired
    public UserAnalyticsService(UserAccountRepository userAccountRepository,
                                DonationPostRepository donationPostRepository,
                                LikesRepository likesRepository) {
        this.userAccountRepository = userAccountRepository;
        this.donationPostRepository = donationPostRepository;
        this.likesRepository = likesRepository;
    }

    public UserAnalyticsDTO getUserAnalytics(Long userId) {
        UserAccount user = userAccountRepository.findById(userId).orElse(null);
        if (user != null) {
            List<DonationPost> donationPosts = donationPostRepository.findAllByUser_UserId(userId);
            List<Likes> likesReceivedOnUserPosts = getLikesReceivedOnUserPosts(userId);
            int totalLikes = likesReceivedOnUserPosts.size();
            int totalPosts = donationPosts.size();
            int likesLastWeek = countLikesLastWeek(likesReceivedOnUserPosts);

            UserAnalyticsDTO userAnalyticsDTO = new UserAnalyticsDTO();
            userAnalyticsDTO.setUserAccount(user);
            userAnalyticsDTO.setDonationPosts(donationPosts);
            userAnalyticsDTO.setLikes(likesReceivedOnUserPosts);
            userAnalyticsDTO.setTotalLikes(totalLikes);
            userAnalyticsDTO.setTotalPosts(totalPosts);
            userAnalyticsDTO.setLikesLastWeek(likesLastWeek);

            return userAnalyticsDTO;
        }
        return null;
    }

    public UserAccount getUser(Long userId) {
        return userAccountRepository.findById(userId).orElse(null);
    }

    public List<DonationPost> getUserPosts(Long userId) {
        return donationPostRepository.findAllByUser_UserId(userId);
    }

    private List<Likes> getLikesReceivedOnUserPosts(Long userId) {
        List<DonationPost> userPosts = getUserPosts(userId);
        List<Long> userPostIds = userPosts.stream().map(DonationPost::getPostId).collect(Collectors.toList());
        return likesRepository.findAllByPost_PostIdIn(userPostIds);
    }

    private int countLikesLastWeek(List<Likes> likes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastWeek = now.minusWeeks(1);
        return (int) likes.stream().filter(like -> like.getLikeDateTime().isAfter(lastWeek)).count();
    }


}
