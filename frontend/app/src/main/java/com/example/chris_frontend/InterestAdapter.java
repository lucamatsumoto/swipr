package com.example.chris_frontend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.InterestHolder>
{
    class InterestHolder extends RecyclerView.ViewHolder
    {
        public final ViewGroup item;
        final InterestAdapter adapter;
        public InterestHolder(ViewGroup aItem, InterestAdapter aAdapter)
        {
            super(aItem);
            item = aItem;
            this.adapter = aAdapter;
        }
    }

    private List<Interest> interestList;
    private LayoutInflater inflater;

    public InterestAdapter(Context context, List<Interest> aInterestArray) {
        Log.d("here", "13");
        inflater = LayoutInflater.from(context);
        Log.d("here", "14");
        this.interestList = aInterestArray;
        Log.d("here", "15");
    }

    @NonNull
    @Override
    public InterestAdapter.InterestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("here", "11");
        ViewGroup item = (ViewGroup) inflater.inflate(R.layout.interest, viewGroup, false);
        Log.d("here", "12");
        return new InterestHolder(item, this);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestAdapter.InterestHolder interestHolder, int i) {
        Interest interest = interestList.get(i);
        TextView statusChild = (TextView) interestHolder.item.getChildAt(1);
        statusChild.setText(interest.status);
    }

    @Override
    public int getItemCount() {
        return interestList.size();
    }
}
