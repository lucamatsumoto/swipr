package com.example.chris_frontend.Shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiningHalls {
    public static final List<String> diningHallList;
    static {
        List<String> temp = new ArrayList<String>(Arrays.asList(
                "Covel",
                "De Neve",
                "Bruin Plate",
                "Feast",
                "Bruin Cafe",
                "Cafe 1919",
                "Rendezvous",
                "The Study"
        ));
        diningHallList = temp;

    }
    public DiningHalls() {}
    public List<String> get(){return diningHallList;}
}
