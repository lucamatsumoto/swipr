package com.example.myapplication.Seller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication.Buyer.BuyerActivity;
import com.example.myapplication.R;
import com.example.myapplication.Shared.DrawerBaseActivity;

public class SellerActivity extends DrawerBaseActivity {

    SeekBar s_price;
    SeekBar s_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("here", "1");
        super.onCreate(savedInstanceState);

        //ADD THESE LINES TO ADD DRAWER FOR PROFILE INFO
        //setContentView(R.layout.activity_buyer);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_seller, null, false);
        Log.d("here", "2");
        dl.addView(contentView, 0);
        //END

        // initiate  views
        setContentView(R.layout.activity_seller);
        int step_value = 50; //Number of cents to change the total by
        s_price.setMax(1500/step_value);
        TextView textView = findViewById(R.id.price_value);
        s_price=findViewById(R.id.s_price);
        // perform seek bar change listener event used for getting the progress value
        s_price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue;
            {
                progressChangedValue = 0;
                textView.setText("$" + String.format("%.2f", s_price.getProgress() / (float) 100));
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int)Math.round(progress/step_value )) * step_value;
                seekBar.setProgress(progress);
                textView.setText("$" + String.format("%.2f", s_price.getProgress() / (float) 100));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void launchBuyerActivity(View view) {
        //launch buyer tab
        Intent intent = new Intent(this, BuyerActivity.class);
        startActivity(intent);
    }
}
