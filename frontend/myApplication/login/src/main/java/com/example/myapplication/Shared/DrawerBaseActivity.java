package com.example.myapplication.Shared;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DrawerBaseActivity extends AppCompatActivity {
    protected DrawerLayout dl;
    protected NetworkManager networkManager;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private GoogleSignInClient mGoogleSignInClient;
    protected ProfileSingleton profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();
        profile = ProfileSingleton.getInstance();

        if (profile.getProfilePicture() == null)
            Log.d("HERE", "NULL Picture");
        else
            Log.d("HERE", profile.getProfilePicture().toString());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
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


        View hView = nv.getHeaderView(0);
        ImageView imgView = (ImageView) hView.findViewById(R.id.profilePic);
        Glide.with(getApplicationContext()).load(profile.getProfilePicture())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgView);

        TextView TV = (TextView) hView.findViewById(R.id.Drawer_name);
        TV.setText(profile.getFirstName() + " " + profile.getLastName());

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
            networkManager.disconnect(this);
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("BITMAP", "Error getting bitmap", e);
        }
        return bm;
    }
}
