package com.example.myapplication.Buyer;

import com.example.myapplication.Buyer.Interest.Interest;
import com.example.myapplication.Shared.Offer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestBacker {
    private List<Interest> interestsList;
    private static volatile InterestBacker instance;
    private Map<Interest, Boolean> isInterestConfirmed;

    private InterestBacker() {
        interestsList = new ArrayList<>();
        isInterestConfirmed = new HashMap<>();

    }

    public static InterestBacker getInstance()
    {
        if(instance == null)
            instance = new InterestBacker();
        return instance;
    }
    public List<Interest> getInterests()
    {
        return interestsList;
    }
    public void addInterests(Interest aInterest){
        interestsList.add(aInterest);
        if (isInterestConfirmed.get(aInterest) == null) {
            isInterestConfirmed.put(aInterest, false);
        }
    }

    public Boolean isConfirmed(Interest i)
    {
        return isInterestConfirmed.get(i);
    }

    public void setConfirmed(Interest interest, Boolean cancelled) {
        isInterestConfirmed.put(interest, cancelled);
    }

}
