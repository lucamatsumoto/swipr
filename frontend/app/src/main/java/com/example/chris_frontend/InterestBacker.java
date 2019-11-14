package com.example.chris_frontend;

import com.example.chris_frontend.Interest.Interest;

import java.util.ArrayList;
import java.util.List;

public class InterestBacker {
    private List<Interest> interestsList;
    private static volatile InterestBacker instance;
    private InterestBacker()
    {
        interestsList = new ArrayList<>();
        for(int k = 0; k < 10; k++)
            interestsList.add(new Interest());
    }
    static InterestBacker getInstance()
    {
        if(instance == null)
            instance = new InterestBacker();
        return instance;
    }
    List<Interest> getInterests()
    {
        return interestsList;
    }
    public void setInterests(List<Interest> aInterests){interestsList = aInterests;}
    public void addInterests(Interest aInterest){interestsList.add(aInterest);}
}
