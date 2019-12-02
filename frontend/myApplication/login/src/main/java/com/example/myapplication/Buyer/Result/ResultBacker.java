package com.example.myapplication.Buyer.Result;

import com.example.myapplication.Shared.Offer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultBacker {

    private static volatile ResultBacker instance;
    private List<Offer> resultsList;
    private Map<Offer, Boolean> isOfferedCancel;

    private ResultBacker() {
        resultsList = new ArrayList<Offer>();
        isOfferedCancel = new HashMap<>();
    }

    public static ResultBacker getInstance()
    {
        if(instance == null)
            instance = new ResultBacker();
        return instance;
    }

    public List<Offer> getResults(){return resultsList;}

    public void addOffer (Offer o){
        resultsList.add(o);
        isOfferedCancel.put(o, false);
    }

    public void clearOffers(){
        resultsList = new ArrayList<Offer>();
        isOfferedCancel.clear();
    }

    public Boolean getCancelled(Offer offer) {
        return isOfferedCancel.get(offer);
    }

    public void setCancelled(Offer offer, Boolean cancelled) {
        isOfferedCancel.put(offer, cancelled);
    }
}
