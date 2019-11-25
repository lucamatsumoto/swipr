package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Shared.DrawerBaseActivity;
import com.example.myapplication.Shared.NetworkManager;
import com.example.myapplication.Shared.NetworkResponder;
import com.example.myapplication.Shared.ProfileSingleton;

import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity {

    ProfileSingleton profile;
    NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkManager = NetworkManager.getInstance();
       // setContentView(R.layout.activity_edit_profile);

        //ADD THESE LINES TO ADD DRAWER FOR PROFILE INFO
        setContentView(R.layout.activity_edit_profile);
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View contentView = inflater.inflate(R.layout.activity_edit_profile, null, false);
//        dl.addView(contentView, 0);
//        //END
        profile = ProfileSingleton.getInstance();

        TextView firstNameTV = findViewById(R.id.usernameFirst);
        firstNameTV.setText(profile.getFirstName());

        TextView lastNameTV = findViewById(R.id.usernameLast);
        lastNameTV.setText(profile.getLastName());


        EditText venmoET = findViewById(R.id.venmo);
        venmoET.setText(profile.getVenmo());

        getSupportActionBar().setTitle("Edit Swipr Account"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar
        ImageView imgView = (ImageView) findViewById(R.id.profilePic);
        Glide.with(getApplicationContext()).load(profile.getProfilePicture())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgView);

        final Button button = findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userString = venmoET.getText().toString().trim();
                if(userString == null || userString.isEmpty())
                    Toast.makeText(getApplicationContext(), "Venmo Username Cannot Be Empty", Toast.LENGTH_SHORT).show();
                else if(userString.charAt(0) != '@')
                    Toast.makeText(getApplicationContext(), "Venmo Username Must Start with '@'", Toast.LENGTH_SHORT).show();
                else
                    saveProfile(venmoET.getText().toString());
            }
        });
        //networkManager.subscribe("/user/queue/reply", new ProfileResponder());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                Toast.makeText(this, "Profile Info Not Saved", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProfile(String newVenmo)
    {
        profile.setVenmo(newVenmo);
        JSONObject json = profile.asJSON();
        try {
            json.put("venmo", newVenmo);
        }
        catch(Exception e)
        {
            Log.e("JSON", e.getMessage());
        }
        networkManager.send("/swipr/updateVenmo", json.toString());
        finish();
        Toast.makeText(this, "Profile Info Saved", Toast.LENGTH_SHORT).show();
    }

    class ProfileResponder implements NetworkResponder
    {
        @Override
        public void onMessageReceived(String json) {
            return;
        }
    }
}
