package com.kindnesskattle.bddAtcProject.Repository;


import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationPostRepository extends JpaRepository<DonationPost, Long> {
    List<DonationPost> findAllByUser_UserId(Long userId);
}
