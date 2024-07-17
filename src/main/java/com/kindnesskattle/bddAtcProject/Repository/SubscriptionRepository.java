package com.kindnesskattle.bddAtcProject.Repository;


import com.kindnesskattle.bddAtcProject.Entities.City;
import com.kindnesskattle.bddAtcProject.Entities.Subscription;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByUserAndCity(UserAccount user, City city);
}
