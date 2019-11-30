package com.example.myapplication.Shared;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.myapplication.R;

public class Popup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout( (int) (width * .5), (int) (height * .5));

    }

}
