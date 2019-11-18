package com.example.chris_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.chris_frontend.Chris_Templates.SimpleSpinAdapter;
import com.example.chris_frontend.Result.ResultAdapter;

public class BuyerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView resultRecycler;
    private ResultAdapter resultAdapter;
    private BuyerBacker buyerBacker;
    private ViewGroup filterFrame;
    private ViewGroup resultFrame;

    private Spinner diningHallSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        buyerBacker = BuyerBacker.getInstance();

        resultRecycler = findViewById(R.id.resultRecycler);
        resultAdapter = new ResultAdapter(this, buyerBacker.getResults());
        resultRecycler.setAdapter(resultAdapter);
        resultRecycler.setLayoutManager(new LinearLayoutManager(this));

        filterFrame = findViewById(R.id.filter_frame);
        resultFrame = findViewById(R.id.result_frame);

        diningHallSpinner = (Spinner) findViewById(R.id.filter_dining_spinner);
        diningHallSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> diningHallAdapter = new SimpleSpinAdapter(this, buyerBacker.getDiningHalls());
        diningHallSpinner.setAdapter(diningHallAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(buyerBacker.isFilter) {
            filterFrame.setVisibility(View.VISIBLE);
            resultFrame.setVisibility(View.GONE);
        }
        else{
            filterFrame.setVisibility(View.GONE);
            resultFrame.setVisibility(View.VISIBLE);
        }
        initSpinnerSelection(diningHallSpinner, 2, buyerBacker.getDiningHallIndex());

    }

    /**
     * Launches the Login Activity.
     * @param view      The java side representation of the UI button that triggered this function call.
     */
    public void launchLoginActivity(View view) {
        //launch login tab
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the Seller activity.
     * @param view      The java side representation of the UI button that triggered this function call.
     */
    public void launchSellerActivity(View view) {
        //launch seller tab.
    }

    /**
     * Launches the Interest sub activity.
     * @param view      The java side representation of the UI button that triggered this function call.
     */
    public void launchInterestsActivity(View view) {
        //launch login tab
        Intent intent = new Intent(this, InterestActivity.class);
        startActivity(intent);
    }

    public void search(View view) {
        filterFrame.setVisibility(View.GONE);
        resultFrame.setVisibility(View.VISIBLE);
        buyerBacker.swapView();
    }

    public void setFilters(View view) {
        filterFrame.setVisibility(View.VISIBLE);
        resultFrame.setVisibility(View.GONE);
        buyerBacker.swapView();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.filter_dining_spinner)
            buyerBacker.setDiningHallIndex(parent.getSelectedItemPosition());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initSpinnerSelection(Spinner spinner, int size, int index)
    {
        if(index >= 0 && index < size)
            spinner.setSelection(index);
    }
}
