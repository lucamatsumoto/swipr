package com.example.chris_frontend.Shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealPeriod {
    public static final List<String> mealPeriodList;
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
        mealPeriodList = temp;

    }
    private MealPeriod() {}
    public static List<String> get(){return mealPeriodList;}
}