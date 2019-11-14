package com.example.chris_frontend.Interest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chris_frontend.R;

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
        inflater = LayoutInflater.from(context);
        this.interestList = aInterestArray;
    }

    @NonNull
    @Override
    public InterestAdapter.InterestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewGroup item = (ViewGroup) inflater.inflate(R.layout.interest, viewGroup, false);
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
