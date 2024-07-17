package com.kindnesskattle.bddAtcProject.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "pickup_completed")
@Data
public class PickupCompleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pickup_status_id")
    private Long pickupStatusId;

    @Column(name = "picked_up_by_user_id", nullable = false)
    private Long pickedUpByUserId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "pickup_date_time", nullable = false)
    private LocalDateTime pickupDateTime;

    // Constructors
    public PickupCompleted() {
    }

    public PickupCompleted(Long pickedUpByUserId, Long postId, LocalDateTime pickupDateTime) {
        this.pickedUpByUserId = pickedUpByUserId;
        this.postId = postId;
        this.pickupDateTime = pickupDateTime;
    }
}
