package com.example.myapplication.Shared;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public abstract class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.SimpleViewHolder>
{

    /**
     * Constructor for a SimpleRecycler Adapter
     * @param context           If you generate this adapter in a AppCompatActivity class,
     *                              pass in "this".
     * @param objectList        The list of objects you want to store in the RecyclerView.
     * @param itemLayout        The xml file that represents the individual object you want
     *                              to store in the Recyclerview.
     *                          Of the form, "R.layout.<layoutFileName>"
     */
    public SimpleRecyclerAdapter(Context context, List<? extends Object> objectList, int itemLayout) {
        inflater = LayoutInflater.from(context);
        this.mObjectList = objectList;
        mItemLayout = itemLayout;
    }

    protected List<? extends Object> mObjectList;
    private LayoutInflater inflater;
	private int mItemLayout;
	
    public class SimpleViewHolder extends RecyclerView.ViewHolder
    {
        public final ViewGroup mItem;
        final SimpleRecyclerAdapter mAdapter;
        public SimpleViewHolder(ViewGroup item, SimpleRecyclerAdapter adapter)
        {
            super(item);
            mItem = item;
            this.mAdapter = adapter;
        }
    }

    @NonNull
    @Override
    public SimpleRecyclerAdapter.SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewGroup item = (ViewGroup) inflater.inflate(mItemLayout, viewGroup, false);
        return new SimpleViewHolder(item, this);
    }

    //This will bind an xml layout item to the java object.
    //What you need to do here is populate the xml's fields with the object's values.
    @Override
    public abstract void onBindViewHolder(@NonNull SimpleRecyclerAdapter.SimpleViewHolder simpleViewHolder, int i);
    //Example Implementation:
        /*
        Offer object = (Offer) mObjectList.get(i);
        TextView statusChild = (TextView) SimpleViewHolder.mItem.getChildAt(1);
        statusChild.setText(Offer.status);
         */

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }
}
