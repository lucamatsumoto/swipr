package com.example.myapplication.Buyer.Interest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.Buyer.Result.ResultBacker;
import com.example.myapplication.Shared.Offer;
import com.example.myapplication.Shared.SimpleRecyclerAdapter;
import com.example.myapplication.R;

import java.util.List;

public class InterestAdapter extends SimpleRecyclerAdapter
{
    public InterestAdapter(Context context, List<Interest> interestList) {
        super(context, interestList, R.layout.interest);
    }

    public void onBindViewHolder(@NonNull SimpleRecyclerAdapter.SimpleViewHolder simpleViewHolder, int i) {
        Offer offer = (Offer) mObjectList.get(i);
        Log.d("CHILDREN", Integer.toString(simpleViewHolder.mItem.getChildCount()));
        for(int j = 0; j < 2; j++) {
            final View child = simpleViewHolder.mItem.getChildAt(j);
            Log.d("CHILDREN", simpleViewHolder.mItem.getChildAt(j).getClass().getName());
        }
        Log.d("Dining Halls", offer.diningHallList.toString());

        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm" );

//        View offerView = simpleViewHolder.mItem.getChildAt(1);
//
//        TextView diningHall_tag = offerView.findViewById(R.id.dining_hall_tag);
//        diningHall_tag.setText((getDiningHallTag(offer)));
//
//        TextView diningHall_val = offerView.findViewById(R.id.dining_hall_value);
//        diningHall_val.setText(getDiningHallText(offer));
//
//        TextView start = offerView.findViewById(R.id.start_time_value);
//        start.setText(offer.startTime.toString());
//        //start.setText(OffsetDateTime.parse(offer.startTime.toString(), dtf).toString());
//
//        TextView end = offerView.findViewById(R.id.end_time_value);
//        end.setText(offer.endTime.toString());
//        //end.setText(OffsetDateTime.parse(offer.endTime.toString(), dtf).toString());
//
//        TextView price = offerView.findViewById(R.id.price_value);
//        price.setText(String.format("$%.02f", offer.price / 100.0));
//        Button interestButton = (Button) simpleViewHolder.mItem.getChildAt(0);
//
//        ResultBacker resultBacker = ResultBacker.getInstance();
//        if (resultBacker.getCancelled(offer)) {
//            interestButton.setText("Cancel");
//        } else {
//            interestButton.setText("I'm Interested");
//        }
//
//        interestButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (!resultBacker.getCancelled(offer)) {
//                    Log.d("Interested", Integer.toString(i));
//                    Intent i = new Intent(view.getContext(), RefineInterestActivity.class);
//                    i.putExtra("Offer", offer.generateQuery());
//                    view.getContext().startActivity(i);
//                } else {
//                    Log.d("Cancelled", Integer.toString(i));
//                    // Send the cancel request over to the server
//                    sendCancelInterest(offer);
//                    resultBacker.setCancelled(offer, false);
//                    interestButton.setText("I'm Interested");
//                }
//            }
//        });
    }
}
