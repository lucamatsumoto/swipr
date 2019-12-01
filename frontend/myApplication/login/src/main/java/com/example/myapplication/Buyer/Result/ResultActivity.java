package com.example.myapplication.Buyer.Result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.myapplication.Buyer.BuyerBacker;
import com.example.myapplication.Buyer.Interest.InterestDialog;
import com.example.myapplication.R;
import com.example.myapplication.Shared.NetworkManager;
import com.example.myapplication.Shared.SimpleSpinAdapter;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView resultRecycler;
    private ResultAdapter resultAdapter;
    private ResultBacker resultBacker;
    private String BuyerQuery;

    private NetworkManager networkManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        networkManager = NetworkManager.getInstance();
        resultBacker = ResultBacker.getInstance();

        resultRecycler = findViewById(R.id.b_resultRecycler);
        resultAdapter = new ResultAdapter(this, resultBacker.getResults());
        resultRecycler.setAdapter(resultAdapter);
        resultRecycler.setLayoutManager(new LinearLayoutManager(this));
        // Add dividers
        resultRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        resultRecycler.setItemAnimator(new DefaultItemAnimator());
        Log.d("RESULTS", resultBacker.getResults().toString());

        BuyerQuery = getIntent().getStringExtra("BuyerQuery");
        // Log.d("BuyerQuery", BuyerQuery);
    }

    public void refresh(View view)
    {
        networkManager.showBuyerUpdate = false;
        ResultBacker.getInstance().clearOffers();
        networkManager.send("/swipr/refreshOffers", BuyerQuery);
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onDestroy() {
        networkManager.showBuyerUpdate = true;
        super.onDestroy();
    }
}
