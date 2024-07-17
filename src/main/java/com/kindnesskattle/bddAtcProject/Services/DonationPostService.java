package com.kindnesskattle.bddAtcProject.Services;



import com.kindnesskattle.bddAtcProject.Repository.DonationPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonationPostService {

    @Autowired
    private DonationPostRepository donationPostRepository;



}
