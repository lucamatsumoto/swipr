package com.example.myapplication.Shared;

import android.util.Log;

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
                "Feast"
                //"Bruin Cafe",
                //"Cafe 1919",
                //"Rendezvous",
                //"The Study"
        ));
        diningHallList = temp;
    }
    private DiningHalls() {}
    public static List<String> get(){return diningHallList;}
    public static List<Boolean> getSelectedFromBitValue(long bitValue)
    {
        long tempBit = bitValue;
        List<Boolean> tempList = new ArrayList<>(diningHallList.size());
        for(int k = 0; k < diningHallList.size(); k++, tempBit /= 2)
            tempList.add(k, (tempBit % 2) == 1);

        return tempList;
    }
    public static long getBitValueFromSelected(List<Boolean> selected)
    {
        long tempBit = 0;
        long base = 1;
        for(int k = 0; k < diningHallList.size(); k++, base *= 2)
            if(selected.get(k))
                tempBit += base;
        return tempBit;
    }
}
