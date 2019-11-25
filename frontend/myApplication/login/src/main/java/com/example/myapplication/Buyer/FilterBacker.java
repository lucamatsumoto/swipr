package com.example.myapplication.Buyer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FilterBacker {
    private static volatile FilterBacker instance;
    private FilterBacker(){diningHallList = new ArrayList<>();}
    public static FilterBacker getInstance()
    {
        if(instance == null)
			instance = new FilterBacker();
        return instance;
    }

    protected List<DiningHall> diningHallList;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected Float Price;

    protected class DiningHall{
        protected boolean isSelected;
        protected String name;
        protected DiningHall(boolean argIsSelected, String argName) {
            isSelected = argIsSelected;
            name = argName;
        }
    }
}
