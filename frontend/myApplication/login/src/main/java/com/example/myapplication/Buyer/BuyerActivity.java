package com.example.myapplication.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.example.myapplication.Seller.SellerActivity;
import com.example.myapplication.Shared.DrawerBaseActivity;
import com.example.myapplication.Shared.NetworkManager;
import com.example.myapplication.Shared.SimpleSpinAdapter;
import com.example.myapplication.Buyer.Result.ResultAdapter;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

public class BuyerActivity extends DrawerBaseActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView resultRecycler;
    private ResultAdapter resultAdapter;
    private BuyerBacker buyerBacker;
    private ViewGroup filterFrame;
    private ViewGroup resultFrame;
    private Spinner diningHallSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ADD THESE LINES TO ADD DRAWER FOR PROFILE INFO
        //setContentView(R.layout.activity_buyer);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_buyer, null, false);
        dl.addView(contentView, 0);
        //END

        networkManager = NetworkManager.getInstance();

        buyerBacker = BuyerBacker.getInstance();

        Intent intent = getIntent();
        String option = intent.getExtras().getString("From");
        buyerBacker.setSignin(option);

        resultRecycler = findViewById(R.id.b_resultRecycler);
        resultAdapter = new ResultAdapter(this, buyerBacker.getResults());
        resultRecycler.setAdapter(resultAdapter);
        resultRecycler.setLayoutManager(new LinearLayoutManager(this));

        filterFrame = findViewById(R.id.b_filter_frame);
        resultFrame = findViewById(R.id.b_result_frame);

        diningHallSpinner = (Spinner) findViewById(R.id.b_filter_dining_spinner);
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
     * Launches the Seller activity.
     * @param view      The java side representation of the UI button that triggered this function call.
     */
    public void launchSellerActivity(View view) {
        //launch login tab
        Log.d("here", "3");
        Intent intent = new Intent(this, SellerActivity.class);
        Log.d("here", "4");
        startActivity(intent);
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
        if(parent.getId() == R.id.b_filter_dining_spinner)
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
