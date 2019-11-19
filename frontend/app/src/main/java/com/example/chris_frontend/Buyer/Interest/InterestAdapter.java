package com.example.chris_frontend.Buyer.Interest;

import android.content.Context;
import android.widget.TextView;

import com.example.chris_frontend.Shared.SimpleRecyclerAdapter;
import com.example.chris_frontend.R;

import java.util.List;

public class InterestAdapter extends SimpleRecyclerAdapter
{
    public InterestAdapter(Context context, List<Interest> interestList) {
        super(context, interestList, R.layout.result);
    }

    public void onBindViewHolder(SimpleRecyclerAdapter.SimpleViewHolder simpleViewHolder, int i) {
        Interest interest = (Interest) mObjectList.get(i);
        TextView statusChild = (TextView) simpleViewHolder.mItem.getChildAt(1);
        statusChild.setText(interest.status);
    }
}
