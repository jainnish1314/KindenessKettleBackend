package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.Entities.City;
import com.kindnesskattle.bddAtcProject.Entities.Subscription;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public void subscribe(UserAccount user, City city) {
        // Check if user is already subscribed to the city
        Subscription existingSubscription = subscriptionRepository.findByUserAndCity(user, city);
        if (existingSubscription == null) {
            Subscription subscription = new Subscription();
            subscription.setUser(user);
            subscription.setCity(city);
            subscriptionRepository.save(subscription);
        }
    }

    public void unsubscribe(UserAccount user, City city) {
        // Find and delete the subscription if it exists
        Subscription existingSubscription = subscriptionRepository.findByUserAndCity(user, city);
        if (existingSubscription != null) {
            subscriptionRepository.delete(existingSubscription);
        }
    }


}
