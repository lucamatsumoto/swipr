package com.example.myapplication.Buyer;

import com.example.myapplication.Buyer.Interest.Interest;

import java.util.ArrayList;
import java.util.List;

public class InterestBacker {
    private List<Interest> interestsList;
    private static volatile InterestBacker instance;
    private InterestBacker() {
        interestsList = new ArrayList<>();
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
    public void setInterests(List<Interest> aInterests){interestsList = aInterests;}
    public void addInterests(Interest aInterest){interestsList.add(aInterest);}
}
