package com.example.chris_frontend;

import java.util.ArrayList;
import java.util.List;

public class Results {
    private volatile List<Offer> resultsList;
    private static volatile Results instance;
    private Results()
    {
        resultsList = new ArrayList<>();
    }
    public static Results getInstance()
    {
        if(instance == null)
            synchronized (Results.class)
            {
                if (instance == null)
                    instance = new Results();
            }
        return instance;
    }
    public List<Offer> getResults()
    {
        return resultsList;
    }
    public void setResults(List<Offer> aResults)
    {
        synchronized (Results.class)
        {
            resultsList = aResults;
        }
    }
    public void addResults(Offer aOffer)
    {
        synchronized (Results.class)
        {
            resultsList.add(aOffer);
        }
    }
}
