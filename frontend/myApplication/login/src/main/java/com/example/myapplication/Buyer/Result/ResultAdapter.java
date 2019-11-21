package com.example.myapplication.Buyer.Result;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.Shared.SimpleRecyclerAdapter;
import com.example.myapplication.Shared.Offer;
import com.example.myapplication.R;

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
