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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Shared.DrawerBaseActivity;
import com.example.myapplication.Shared.ProfileSingleton;

public class EditProfileActivity extends AppCompatActivity {

    ProfileSingleton profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        finish();
        Toast.makeText(this, "Profile Info Saved", Toast.LENGTH_SHORT).show();
    }
}
