package com.example.myapplication.Shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiningHalls {
    public static final List<String> diningHallList;
    static {
        //The order here actually matters, and corresponds
        //to the bitfield order in the backend.
        List<String> temp = new ArrayList<String>(Arrays.asList(
                "Bruin Plate",
                "Covel",
                "De Neve",
                "Feast",
                "Bruin Cafe",
                "Cafe 1919",
                "Rendezvous",
                "The Study"
        ));
        diningHallList = temp;

    }
    private DiningHalls() {}
    public static List<String> get(){return diningHallList;}
    public static List<String> getNameFromBitField(long bitValue)
    {
        if(bitValue < 0 || bitValue >= diningHallList.size())
            return null;
        long tempBit = bitValue;
        List<String> tempList = new ArrayList<>();
        if((tempBit %= 2) == 1)
            tempList.add(diningHallList.get(0));
        if((tempBit %= 2) == 1)
            tempList.add(diningHallList.get(0));
        if((tempBit %= 2) == 1)
            tempList.add(diningHallList.get(0));
        if((tempBit %= 2) == 1)
            tempList.add(diningHallList.get(0));
        return tempList;
    }
}
