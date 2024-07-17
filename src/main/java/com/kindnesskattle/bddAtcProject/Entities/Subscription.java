package com.kindnesskattle.bddAtcProject.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Subscription")
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}
