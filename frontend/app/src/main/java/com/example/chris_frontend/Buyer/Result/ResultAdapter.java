package com.example.chris_frontend.Buyer.Result;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.chris_frontend.Shared.SimpleRecyclerAdapter;
import com.example.chris_frontend.Shared.Offer;
import com.example.chris_frontend.R;

import java.util.List;

public class ResultAdapter extends SimpleRecyclerAdapter
{

    public ResultAdapter(Context context, List<Offer> resultArray) {
        super(context, resultArray, R.layout.result);
    }

    public void onBindViewHolder(@NonNull SimpleRecyclerAdapter.SimpleViewHolder simpleViewHolder, int i) {
        Offer offer = (Offer) mObjectList.get(i);
        TextView statusChild = (TextView) simpleViewHolder.mItem.getChildAt(1);
        //statusChild.setText(offer.status);
    }
}
