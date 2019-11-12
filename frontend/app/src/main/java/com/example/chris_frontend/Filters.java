package com.example.chris_frontend;

import java.util.ArrayList;
import java.util.List;

public class Filters {
    private static volatile Filters instance;
    private Filters()
    {}
    public static Filters getInstance()
    {
        if(instance == null)
            synchronized (Filters.class)
            {
                if (instance == null)
                    instance = new Filters();
            }
        return instance;
    }
}
