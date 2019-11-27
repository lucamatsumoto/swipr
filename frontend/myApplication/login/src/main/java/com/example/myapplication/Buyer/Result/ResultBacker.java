package com.example.myapplication.Buyer.Result;

import com.example.myapplication.Shared.Offer;

import java.util.ArrayList;
import java.util.List;

public class ResultBacker {

    private static volatile ResultBacker instance;
    private List<Offer> resultsList;

    private ResultBacker()
    {
        resultsList = new ArrayList<Offer>();
    }
    public static ResultBacker getInstance()
    {
        if(instance == null)
            instance = new ResultBacker();
        return instance;
    }

    public List<Offer> getResults(){return resultsList;}

    public void addOffer (Offer o){ resultsList.add(o);}

    public void clearOffers(){ resultsList = new ArrayList<Offer>();}
}
