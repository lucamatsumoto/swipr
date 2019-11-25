package com.example.myapplication.Shared;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class SimpleSpinAdapter extends ArrayAdapter<String>
{
    public SimpleSpinAdapter(Context context, List<String> options)
    {
        super(context, android.R.layout.simple_spinner_item, options);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
