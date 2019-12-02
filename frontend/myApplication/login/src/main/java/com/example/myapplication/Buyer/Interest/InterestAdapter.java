package com.example.myapplication.Buyer.Interest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Buyer.Result.ResultBacker;
import com.example.myapplication.Shared.NetworkManager;
import com.example.myapplication.Shared.Offer;
import com.example.myapplication.Shared.SimpleRecyclerAdapter;
import com.example.myapplication.R;

import java.time.LocalDateTime;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InterestAdapter extends SimpleRecyclerAdapter
{
    public InterestAdapter(Context context, List<Interest> interestList) {
        super(context, interestList, R.layout.interest);
    }

    public void onBindViewHolder(@NonNull SimpleRecyclerAdapter.SimpleViewHolder simpleViewHolder, int i) {
        Interest interest = (Interest) mObjectList.get(i);
        Log.d("CHILDREN", Integer.toString(simpleViewHolder.mItem.getChildCount()));
        for(int j = 0; j < 3; j++) {
            final View child = simpleViewHolder.mItem.getChildAt(j);
            Log.d("CHILDREN", simpleViewHolder.mItem.getChildAt(j).getClass().getName());
        }

        View InterestView = simpleViewHolder.mItem.getChildAt(1);

        TextView diningHall_value = InterestView.findViewById(R.id.preferred_dining_hall_value);
        diningHall_value.setText(interest.getPreferredDiningHall());


        TextView meetTime = InterestView.findViewById(R.id.meet_time_value);
        meetTime.setText(convertToHourMinutes(interest.meetTime));

        TextView nameView = InterestView.findViewById(R.id.name_value);
        nameView.setText(interest.name);

        TextView venmoView = InterestView.findViewById(R.id.venmo_value);
        venmoView.setText(interest.venmo);


        Button confirmButton = (Button) simpleViewHolder.mItem.getChildAt(0);

        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                NetworkManager.getInstance().send("/swipr/confirmInterest", interest.Buyer.toString());
            }
        });

        ImageView imgView = (ImageView) simpleViewHolder.mItem.getChildAt(2);
        Glide.with(getApplicationContext()).load(interest.profilePicture)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgView);
    }

    private String convertToHourMinutes(LocalDateTime time) {
        int hour = time.getHour();
        String hourString = hour < 9 ? String.format("0%d", hour) : String.format("%d", hour);
        int minute = time.getMinute();
        String minuteString = minute < 9 ? String.format("0%d", minute) : String.format("%d", minute);
        return String.format("%s:%s", hourString, minuteString);
    }
}
