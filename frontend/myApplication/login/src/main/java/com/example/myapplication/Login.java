package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.chris_frontend.Buyer.BuyerActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "Login";
    private static final String EMAIL = "email";
    private GoogleSignInClient mGoogleSignInClient;
    // FOR FACEBOOK LOGIN
    CallbackManager callbackManager;
    LoginButton loginButton;

    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
         findViewById(R.id.sign_in_button).setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.fb_login);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        AccessToken at = loginResult.getAccessToken();
                        //loginResult.getRecentlyDeniedPermissions()
                        //loginResult.getRecentlyGrantedPermissions()
                        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                        Log.d("API123", loggedIn + " ??");
                        handleSignInResult(at);
                        UpdateUI(at);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });


        try {
            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://157.245.235.19:3000/index");
            mStompClient.connect();
        }
        catch(Exception e)
        {
            Log.e("STOMP_FAIL", "ERROR connecting to server");
        }

        compositeDisposable = new CompositeDisposable();

        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.i("Success", "Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.i("Close", "Stomp connection closed");
                            break;
                    }
                });
        Disposable dispTopic = mStompClient.topic("/user/queue/reply")
                .subscribeOn(Schedulers.io())
                .subscribe(topicMessage -> {
                    Log.d("SubSuccess", "Received " + topicMessage.getPayload());
                    //TODO: create user from login info
                }, throwable -> {
                    Log.e("SubFail", "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispLifecycle);

        compositeDisposable.add(dispTopic);
    }


    protected void onStart()
    {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null)
            updateUI(account);
    }

    @Override
    public void onClick(View v) {
       // switch (v.getId()) { //switch cases dont work for some reason
            if(v.getId() ==  R.id.sign_in_button)
                signIn();
              //  break;
//            case R.id.fb_login:
//                fbSignIn();
//                break;
       // }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    /**
     *  Method for when after user signs in from Facebook. Gathers necessary information and sends
     *  to updateUI() and packageJSON().
     * @param at This is an AccessToken from the Facebook API which is used to gather the
     *           information from the user's public profile.
     */
    private void handleSignInResult(AccessToken at)
    {   Profile profile = Profile.getCurrentProfile();

        GraphRequest request = GraphRequest.newMeRequest(
                at,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try{
                            String firstName = object.getString("first_name");
                            String lastName = object.getString("last_name");
                            String email = object.getString("email");
                            packageJSON(firstName, lastName, email);
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Method for when after user signs in from Google. Gathers necessary information and sends to
     * updateUI() and packageJSON().
     * @param completedTask This is google's representation of a user's sign in information
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String firstName = account.getGivenName();
            String lastName = account.getFamilyName();
            String email = account.getEmail();
            packageJSON(firstName, lastName, email);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
            //System.out.println(account.getDisplayName());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(GoogleSignInAccount account)
    {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        String name = account.getDisplayName();
        intent.putExtra("ID", name);
        intent.putExtra("From", "google");
        startActivity(intent);
    }
    private void UpdateUI(AccessToken at) {

        String ID = at.getUserId();
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra("ID", ID);
        intent.putExtra("From", "fb");
        startActivity(intent);
    }

    /**
     * Obtains strings from handleSignInRequest(), packages them into a JSON and sends to backend
     * using HTTP request.
     * @param firstName
     * @param lastName
     * @param email
     */
    private void packageJSON(String firstName, String lastName, String email)
    {

        String TAG =  "INFO";
        Log.i(TAG, "FirstName = " + firstName);
        Log.i(TAG, "lastName = " + lastName);
        Log.i(TAG, "email = " + email);

        JSONObject json = new JSONObject();

        try
        {
            json.put("firstName", firstName);
            json.put("lastName", lastName);
            json.put("email", email);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


        //Fails somewhere here:
//        try {
//            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://157.245.235.19:3000/index");
//            mStompClient.connect();
//        }
//        catch(Exception e)
//        {
//            Log.e("STOMP_FAIL", "ERROR connecting to server");
//            return;
//        }
        compositeDisposable.add(mStompClient.send("/swipr/create", json.toString())
                .subscribe(() -> {
                    Log.d("SendSuccess", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("SendFail", "Error send STOMP echo", throwable);
                }));
//        try {
//            mStompClient.topic("/user/queue/reply").subscribe(topicMessage -> {
//                        Log.d("loginSuccess", topicMessage.getPayload());
//                    }, throwable -> {
//                        Log.e("loginFail", "throwable " + throwable.getMessage());
//                    }
//
//            );
//        }
//        catch(Exception e)
//        {
//            Log.e("login Fail", e.getMessage());
//            return;
//        }

//        mStompClient.send("/swipr/create", json.toString()).subscribe();
        //to here.
   }
    @Override
    protected void onDestroy() {
        mStompClient.disconnect();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }

        super.onDestroy();
    }

}

