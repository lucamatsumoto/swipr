package com.example.myapplication.Buyer.Interest;

import android.content.Context;
import android.widget.TextView;

import com.example.myapplication.Shared.SimpleRecyclerAdapter;
import com.example.myapplication.R;

import java.util.List;

public class InterestAdapter extends SimpleRecyclerAdapter
{
    public InterestAdapter(Context context, List<Interest> interestList) {
        super(context, interestList, R.layout.result);
    }

    public void onBindViewHolder(SimpleRecyclerAdapter.SimpleViewHolder simpleViewHolder, int i) {
        Interest interest = (Interest) mObjectList.get(i);
    }
}
