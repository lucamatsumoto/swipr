package com.example.myapplication.Buyer.Result;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.Buyer.Interest.InterestDialog;
import com.example.myapplication.Buyer.Interest.RefineInterestActivity;
import com.example.myapplication.Buyer.InterestActivity;
import com.example.myapplication.Shared.DiningHalls;
import com.example.myapplication.Shared.NetworkManager;
import com.example.myapplication.Shared.ProfileSingleton;
import com.example.myapplication.Shared.SimpleRecyclerAdapter;
import com.example.myapplication.Shared.Offer;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ResultAdapter extends SimpleRecyclerAdapter
{
    private Context m_context;
    private NetworkManager networkManager = NetworkManager.getInstance();

    public ResultAdapter(Context context, List<Offer> resultArray) {

        super(context, resultArray, R.layout.result);
        m_context = context;
    }

    private String convertToHourMinutes(LocalDateTime time) {
        int hour = time.getHour();
        String hourString = hour < 9 ? String.format("0%d", hour) : String.format("%d", hour);
        int minute = time.getMinute();
        String minuteString = minute < 9 ? String.format("0%d", minute) : String.format("%d", minute);
        return String.format("%s:%s", hourString, minuteString);
    }

    public void onBindViewHolder(@NonNull SimpleRecyclerAdapter.SimpleViewHolder simpleViewHolder, int i) {
        Offer offer = (Offer) mObjectList.get(i);
        Log.d("CHILDREN", Integer.toString(simpleViewHolder.mItem.getChildCount()));
        for(int j = 0; j < 2; j++) {
            final View child = simpleViewHolder.mItem.getChildAt(j);
            Log.d("CHILDREN", simpleViewHolder.mItem.getChildAt(j).getClass().getName());
        }
        Log.d("Dining Halls", offer.diningHallList.toString());

        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm" );

        View offerView = simpleViewHolder.mItem.getChildAt(1);

        TextView diningHall_tag = offerView.findViewById(R.id.dining_hall_tag);
        diningHall_tag.setText((getDiningHallTag(offer)));

        TextView diningHall_val = offerView.findViewById(R.id.dining_hall_value);
        diningHall_val.setText(getDiningHallText(offer));

        TextView start = offerView.findViewById(R.id.start_time_value);

        start.setText(convertToHourMinutes(offer.startTime));
        //start.setText(OffsetDateTime.parse(offer.startTime.toString(), dtf).toString());

        TextView end = offerView.findViewById(R.id.end_time_value);
        end.setText(convertToHourMinutes(offer.endTime));
        //end.setText(OffsetDateTime.parse(offer.endTime.toString(), dtf).toString());

        TextView price = offerView.findViewById(R.id.price_value);
        price.setText(String.format("$%.02f", offer.price / 100.0));
        Button interestButton = (Button) simpleViewHolder.mItem.getChildAt(0);

        ResultBacker resultBacker = ResultBacker.getInstance();
        if (resultBacker.getCancelled(offer)) {
            interestButton.setText("Cancel");
        } else {
            interestButton.setText("I'm Interested");
        }

        interestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!resultBacker.getCancelled(offer)) {
                    Log.d("Interested", Integer.toString(i));
                    Intent i = new Intent(view.getContext(), RefineInterestActivity.class);
                    i.putExtra("Offer", offer.generateQuery());
                    view.getContext().startActivity(i);
                } else {
                    Log.d("Cancelled", Integer.toString(i));
                    // Send the cancel request over to the server
                    sendCancelInterest(offer);
                    resultBacker.setCancelled(offer, false);
                    interestButton.setText("I'm Interested");
                }
            }
        });
    }


    public String getDiningHallText(Offer offer){
        Log.d("Dining Halls", (offer.diningHallList).toString());
        String returnString = "";
        for(int i = 0; i < offer.diningHallList.size(); i ++) {
            if(offer.diningHallList.get(i))
                returnString += DiningHalls.diningHallList.get(i) + "\n";
        }
        return returnString.trim();
    }
    public String getDiningHallTag(Offer offer){
        String returnString = "DiningHall(s)";
        int numDiningHalls = 0;
        for(int i = 0; i < offer.diningHallList.size(); i++) {
            if(offer.diningHallList.get(i))
                numDiningHalls++;
        }
        for(int i = 0; i < numDiningHalls - 1; i++)
                returnString += "\n";
        return returnString;
    }

    public void showInterestDialog(List<Boolean> diningHalls) {

        InterestDialog dialogFragment = new InterestDialog();
        dialogFragment.setDiningHalls(diningHalls);
        dialogFragment.show(((FragmentActivity) m_context).getSupportFragmentManager(), "Interested Button");
        Log.d("Interested Button", "interest clicked");
    }

    private void sendCancelInterest(Offer offer)
    {
        JSONObject interestJSON = new JSONObject();
        String offerjsonString = offer.generateQuery();
        JSONObject offerJSON = null;
        try {
            offerJSON = new JSONObject(offerjsonString);
        } catch (JSONException e) {
            Log.e("JSON ERR", e.getLocalizedMessage());
        }

        try
        {
            interestJSON.put("buyerId", ProfileSingleton.getInstance().getID());
            interestJSON.put("meetTime", 0);
            interestJSON.put("preferredDiningHall", 0);
            interestJSON.put("sellQuery", offerJSON);
        }
        catch (JSONException e)
        {
            Log.d("JSON ERROR", e.getMessage());
        }
        // Log.d("JSON from buyer", interestJSON.toString());
        networkManager.send("/swipr/cancelInterest", interestJSON.toString());
    }
}


