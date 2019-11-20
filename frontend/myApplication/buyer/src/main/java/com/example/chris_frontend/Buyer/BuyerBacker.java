package com.example.chris_frontend.Buyer;

import com.example.chris_frontend.Shared.Offer;

import java.util.ArrayList;
import java.util.List;

public class BuyerBacker {
    private List<Offer> resultsList;
    private List<String> diningHallList;
    private int diningHallIndex;
    boolean isResults;
    boolean isFilter;
    private static volatile BuyerBacker instance;
    private BuyerBacker()
    {
        resultsList = new ArrayList<>();
        for(int k = 0; k < 10; k++)
            resultsList.add(new Offer());

        isFilter = true;
        isResults = false;
        diningHallList = new ArrayList<>();
        diningHallList.add("B Plate");
        diningHallList.add("Covel");
    }
    static BuyerBacker getInstance()
    {
        if(instance == null)
            instance = new BuyerBacker();
        return instance;
    }
    List<Offer> getResults()
    {
        return resultsList;
    }
    List<String> getDiningHalls()
    {
        return diningHallList;
    }
    int getDiningHallIndex() {return diningHallIndex;}
    void setDiningHallIndex(int newIndex){diningHallIndex = newIndex;}
    public void setResults(List<Offer> aResults){resultsList = aResults;}
    public void addResults(Offer aOffer){resultsList.add(aOffer);}
    void swapView(){isResults = !isResults; isFilter = !isFilter;}
}