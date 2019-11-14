package com.example.chris_frontend.Result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chris_frontend.Offer;
import com.example.chris_frontend.R;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultHolder>
{
    class ResultHolder extends RecyclerView.ViewHolder
    {
        public final ViewGroup item;
        final ResultAdapter adapter;
        public ResultHolder(ViewGroup aItem, ResultAdapter aAdapter)
        {
            super(aItem);
            item = aItem;
            this.adapter = aAdapter;
        }
    }

    private List<Offer> resultList;
    private LayoutInflater inflater;

    public ResultAdapter(Context context, List<Offer> aResultArray) {
        inflater = LayoutInflater.from(context);
        this.resultList = aResultArray;
    }

    @NonNull
    @Override
    public ResultAdapter.ResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewGroup item = (ViewGroup) inflater.inflate(R.layout.result, viewGroup, false);
        return new ResultHolder(item, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ResultHolder resultHolder, int i) {
        Offer result = resultList.get(i);
        //TextView statusChild = (TextView) resultHolder.item.getChildAt(1);
        //statusChild.setText(result.status);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }
}
