package com.example.myapplication.Seller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.innovattic.rangeseekbar.RangeSeekBar;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.chip.Chip;
import com.example.myapplication.Buyer.BuyerActivity;
import com.example.myapplication.R;
import com.example.myapplication.Shared.DrawerBaseActivity;

public class SellerActivity extends DrawerBaseActivity {

    SeekBar s_price;
    RangeSeekBar s_time;
    RangeSeekBar.SeekBarChangeListener L;
    String TAG;


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

        // Sliders
        int step_value_price = 50; //Number of cents to change the total by
        TextView textView_price = findViewById(R.id.price_value);
        s_price=findViewById(R.id.s_price);
        s_price.setMax(1500);
        // perform seek bar change listener event used for getting the progress value
        s_price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            {
                s_price.setProgress(800);
                textView_price.setText("$" + String.format("%.2f", s_price.getProgress() / (float) 100));
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int)Math.round(progress/step_value_price )) * step_value_price;
                seekBar.setProgress(progress);
                textView_price.setText("$" + String.format("%.2f", s_price.getProgress() / (float) 100));

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //int step_value_time = 30; //Number of min per step
        s_time=findViewById(R.id.s_time);
        TextView textView_time = findViewById(R.id.time_value);
        s_time.setMax(47); //Minutes for 23:59 hours
        s_time.setMinThumbValue(16);
        s_time.setMaxThumbValue(24);
        s_time.setMinRange(1);
        // perform seek bar change listener event used for getting the progress value
        textView_time.setText("8:00 - 12:00");
        L = new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {
                Log.i(TAG, "Started seeking.");
            }

            @Override
            public void onStoppedSeeking() {
                Log.i(TAG, "Stopped seeking.");
            }

            @Override
            public void onValueChanged(int left_thumb, int right_thumb) {
                int left_thumb_value = left_thumb * 30;
                int right_thumb_value = right_thumb * 30;
                String left_hours = String.format("%2d", left_thumb_value / 60);
                String left_mins = String.format("%02d", left_thumb_value % 60);
                String right_hours = String.format("%2d", right_thumb_value / 60);
                String right_mins = String.format("%02d", right_thumb_value % 60);
                textView_time.setText(left_hours + ":" + left_mins + " - " + right_hours + ":" + right_mins);
            }
        };
        s_time.setSeekBarChangeListener(L);
    }

    public void launchBuyerActivity(View view) {
        //launch buyer tab
        Intent intent = new Intent(this, BuyerActivity.class);
        startActivity(intent);
    }
}
