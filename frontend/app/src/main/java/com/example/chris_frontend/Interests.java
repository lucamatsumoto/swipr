package com.example.chris_frontend;

import java.util.ArrayList;
import java.util.List;

public class Interests {
    private volatile List<Interest> interestsList;
    private static volatile Interests instance;
    private Interests()
    {
        interestsList = new ArrayList<>();
        interestsList.add(new Interest());
    }
    public static Interests getInstance()
    {
        if(instance == null)
            synchronized (Interests.class)
            {
                if (instance == null)
                    instance = new Interests();
            }
        return instance;
    }
    public List<Interest> getInterests()
    {
        return interestsList;
    }
    public void setInterests(List<Interest> aInterests)
    {
        synchronized (Interests.class)
        {
            interestsList = aInterests;
        }
    }
    public void addInterests(Interest aInterest)
    {
        synchronized (Interests.class)
        {
            interestsList.add(aInterest);
        }
    }
}
