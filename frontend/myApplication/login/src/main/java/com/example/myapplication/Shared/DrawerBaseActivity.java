package com.example.myapplication.Shared;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.Buyer.BuyerBacker;
import com.example.myapplication.Buyer.MainActivity;
import com.example.myapplication.EditProfileActivity;
import com.example.myapplication.Login;
import com.example.myapplication.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity {
    protected DrawerLayout dl;
    protected NetworkManager networkManager;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private GoogleSignInClient mGoogleSignInClient;
    private ProfileSingleton profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        ImageView imgView = (ImageView) findViewById(R.id.profilePic);
        if(profile.getProfilePicture() != null)
            imgView.setImageURI(profile.getProfilePicture());
        else
            //imgView.setImageDrawable(R.drawable.swipr_logo);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.profile:
                        launchProfileActivity();
                        break;
                    case R.id.offers:
                        //Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drawer_logout:
                        launchLoginActivity();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void launchProfileActivity(){
        Log.d("HERE", "launching profile page");
        Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the Login Activity.
     *
     */
    public void launchLoginActivity() {
        //launch login tab;
        String option = ProfileSingleton.getInstance().getSignin();
        if(option.equals("google"))
            signOutGoogle();
        else if(option.equals("fb"))
            signOutFacebook();
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
        if(networkManager != null)
            networkManager.disconnect();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
