package com.kindnesskattle.bddAtcProject.DTO;

import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.Likes;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserAnalyticsDTO {
    private UserAccount userAccount;
    private List<DonationPost> donationPosts;
    private List<Likes> likes;
    private int totalLikes;
    private int totalPosts;
    private int likesLastWeek;
}
