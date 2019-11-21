package com.example.myapplication.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.example.myapplication.Shared.NetworkManager;
import com.example.myapplication.Shared.SimpleSpinAdapter;
import com.example.myapplication.Buyer.Result.ResultAdapter;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class BuyerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView resultRecycler;
    private ResultAdapter resultAdapter;
    private BuyerBacker buyerBacker;
    private ViewGroup filterFrame;
    private ViewGroup resultFrame;
    private GoogleSignInClient mGoogleSignInClient;
    private Spinner diningHallSpinner;
    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        networkManager = NetworkManager.getInstance();

        buyerBacker = BuyerBacker.getInstance();

        Intent intent = getIntent();
        String option = intent.getExtras().getString("From");
        buyerBacker.setSignin(option);

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


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
        String option = buyerBacker.getSignin();
        if(option.equals("google"))
            signOutGoogle();
        else if(option.equals("fb"))
            signOutFacebook();
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


    private void signOutGoogle() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        returnToLogin();

                    }
                });
    }

    private void signOutFacebook()
    {
        LoginManager.getInstance().logOut();
        returnToLogin();
    }


    private void returnToLogin()
    {
        networkManager.disconnect();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
