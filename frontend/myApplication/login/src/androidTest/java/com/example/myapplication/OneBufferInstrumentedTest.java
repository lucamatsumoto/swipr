package com.example.myapplication;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.Shared.NetworkResponder;
import com.example.myapplication.Shared.Offer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class OneBufferInstrumentedTest {
    OneBufferResponder reply = new OneBufferResponder("/user/queue/reply");
    OneBufferResponder error = new OneBufferResponder("/user/queue/error");
    OneBufferResponder average = new OneBufferResponder("/topic/average");
    OneBufferResponder sellerUpdate = new OneBufferResponder("/user/queue/sellerUpdate");
    OneBufferResponder sellerInterest = new OneBufferResponder("/user/queue/sellerInterest");
    OneBufferResponder sellerCancel = new OneBufferResponder("/user/queue/sellerCancel");
    OneBufferResponder buyerFind = new OneBufferResponder("/user/queue/buyerFind");
    OneBufferResponder buyerInterest = new OneBufferResponder("/user/queue/buyerInterest");
    private StompClient stompClientBuyer;
    private CompositeDisposable compositeDisposableBuyer;
    private StompClient stompClientSeller;
    private CompositeDisposable compositeDisposableSeller;
    private String resultsTag = "results";
    private String bar = "==================================================================";
    private String tagSeller = "seller";
    private String tagBuyer = "buyer";
    private int buyerId = 1304;
    boolean init = false;

    //TODO: get the Buyer Id dynamically after the call to create()

    @Test
    public void updateVenmo_delete_create()
    {
        init();
        String TAG1 = "updateVenmo";
        JSONObject requestPayload = new JSONObject();
        try {
            requestPayload.put("firstName", "Ryan");
            requestPayload.put("lastName", "Miyahara");
            requestPayload.put("email", "hohohoitsryan@gmail.com");
            requestPayload.put("venmo", "this is my venmo");
        } catch (Exception e) {Log.d(TAG1, e.getMessage());}
        String expectedResponsePayload =
                "{\"id\":null,\"firstName\":\"Ryan\",\"lastName\":\"Miyahara\",\"email\":\"hohohoitsryan@gmail.com\",\"venmo\":\"this is my venmo\",\"profilePicUrl\":null}";
        send(stompClientBuyer, compositeDisposableBuyer, "/swipr/updateVenmo", requestPayload.toString(), TAG1);
        assertionWrapper(expectedResponsePayload, reply.getBuffer(), TAG1);

        String TAG2 = "delete";
        requestPayload = new JSONObject();
        try {
            requestPayload.put("id", 1289);
            requestPayload.put("firstName", "Ryan");
            requestPayload.put("lastName", "Miyahara");
            requestPayload.put("email", "hohohoitsryan@gmail.com");
        } catch (Exception e) {Log.d(TAG1, e.getMessage());}
        expectedResponsePayload =
                "deleted user with email hohohoitsryan@gmail.com";
        send(stompClientBuyer, compositeDisposableBuyer, "/swipr/delete", requestPayload.toString(), TAG2);
        assertionWrapper(expectedResponsePayload, reply.getBuffer(), TAG2);

/*
        String TAG3 = "create";
        requestPayload = new JSONObject();
        try {
            requestPayload.put("firstName", "Ryan");
            requestPayload.put("lastName", "Miyahara");
            requestPayload.put("email", "hohohoitsryan@gmail.com");
        } catch (Exception e) {Log.d(TAG3, e.getMessage());}
        send(stompClientBuyer, compositeDisposableBuyer, "/swipr/create", requestPayload.toString(), TAG3);
        String id = "here";
        String responseString = reply.getBuffer();
        try {
            JSONObject responsePayload = new JSONObject(responseString);
            buyerId = responsePayload.getInt("id");
            id = String.valueOf(buyerId);
        } catch (Exception e) {
            Log.d("here there everywhere:", e.getMessage());
        }
        expectedResponsePayload =
                "{\"id\":"+ id + ",\"firstName\":\"Ryan\",\"lastName\":\"Miyahara\",\"email\":\"hohohoitsryan@gmail.com\",\"venmo\":\"this is my venmo\",\"profilePicUrl\":null}";
        assertionWrapper(expectedResponsePayload, responseString, TAG3);

 */

    }


    //@Test
    public void updateOffer_findOffer_updateOffer_refreshOffers_cancelOffer_findOffer()
    {
        LocalDateTime ldtTime = LocalDateTime.now();
        long epochTime = ldtTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        LocalDateTime ldtTimePlus1 = ldtTime.plusHours(1);
        long epochTimePlus1 = ldtTimePlus1.atZone(ZoneId.systemDefault()).toEpochSecond();
        LocalDateTime ldtTimePlus2 = ldtTime.plusHours(2);
        long epochTimePlus2 = ldtTimePlus2.atZone(ZoneId.systemDefault()).toEpochSecond();

        init();
        String TAG1 = "updateOffer";
        Offer offer = new Offer();
        offer.price = 420;
        offer.startTime = ldtTime;
        offer.endTime = ldtTimePlus2;
        offer.diningHallList = new ArrayList<>();
        offer.diningHallList.add(true);
        offer.diningHallList.add(false);
        offer.diningHallList.add(false);
        offer.diningHallList.add(false);
        offer.userId = 7;
        String expectedResponsePayload =
                "Offer successfully updated";
        send(stompClientSeller, compositeDisposableSeller, "/swipr/updateOffer", offer.generateQuery(), TAG1);
        assertionWrapper(expectedResponsePayload, sellerUpdate.getBuffer(), TAG1);


        String TAG2 = "findOffer";
        offer.endTime = ldtTimePlus1;
        offer.userId = buyerId;
        send(stompClientBuyer, compositeDisposableBuyer, "/swipr/findOffers", offer.generateQuery(), TAG2);
        String payload2 = buyerFind.getBuffer();
        long offerId = 1;
        try {
            JSONArray payloadJSONArray2 = new JSONArray(payload2);
            JSONObject payloadJSONObject2 = (JSONObject) payloadJSONArray2.get(0);
            offerId = payloadJSONObject2.getLong("offerId");
        } catch (Exception e) {Log.d("here there everywhere:", e.getMessage());}
        expectedResponsePayload = "[{\"userId\":7,\"timeRangeStart\":"+ Long.valueOf(epochTime)
                + ",\"timeRangeEnd\":" + Long.valueOf(epochTimePlus2) + ",\"priceCents\":420,\"diningHallBitfield\":1,\"offerId\":" + Long.valueOf(offerId) + "}]";
        assertionWrapper(expectedResponsePayload, payload2, TAG2);

        String TAG3 = "updateOffer2";
        offer.price = 419;
        offer.userId = 7;
        expectedResponsePayload =
                "Offer successfully updated";
        send(stompClientSeller, compositeDisposableSeller, "/swipr/updateOffer", offer.generateQuery(), TAG3);
        assertionWrapper(expectedResponsePayload, sellerUpdate.getBuffer(), TAG3);

        String TAG4 = "refreshOffers";
        offer.userId = buyerId;
        expectedResponsePayload = "[{\"userId\":7,\"timeRangeStart\":"+ Long.valueOf(epochTime)
                + ",\"timeRangeEnd\":" + Long.valueOf(epochTimePlus1) + ",\"priceCents\":419,\"diningHallBitfield\":1,\"offerId\":" + Long.valueOf(offerId + 1) + "}]";
        send(stompClientBuyer, compositeDisposableBuyer, "/swipr/refreshOffers", offer.generateQuery(), TAG4);
        assertionWrapper(expectedResponsePayload, buyerFind.getBuffer(), TAG4);

        String TAG5 = "cancelOffer";
        expectedResponsePayload =
                "Your offer has been cancelled";
        send(stompClientSeller, compositeDisposableSeller, "/swipr/cancelOffer", TAG3);
        assertionWrapper(expectedResponsePayload, sellerCancel.getBuffer(), TAG5);

        String TAG6 = "refreshOffers";
        send(stompClientBuyer, compositeDisposableBuyer, "/swipr/refreshOffers", offer.generateQuery(), TAG6);
        expectedResponsePayload = "[]";
        assertionWrapper(expectedResponsePayload, buyerFind.getBuffer(), TAG6);
    }

    //@Test
    public void updateOffer_findOffer_showInterest_cancelInterest_showInterest_confirmInterest()
    {
        LocalDateTime ldtTime = LocalDateTime.now();
        long epochTime = ldtTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        LocalDateTime ldtTimePlus1 = ldtTime.plusHours(1);
        long epochTimePlus1 = ldtTimePlus1.atZone(ZoneId.systemDefault()).toEpochSecond();
        LocalDateTime ldtTimePlus2 = ldtTime.plusHours(2);
        long epochTimePlus2 = ldtTimePlus2.atZone(ZoneId.systemDefault()).toEpochSecond();

        init();
        String TAG1 = "updateOffer";
        Offer offer = new Offer();
        offer.price = 420;
        offer.startTime = ldtTime;
        offer.endTime = ldtTimePlus2;
        offer.diningHallList = new ArrayList<>();
        offer.diningHallList.add(true);
        offer.diningHallList.add(false);
        offer.diningHallList.add(false);
        offer.diningHallList.add(false);
        offer.userId = 7;
        String expectedResponsePayload =
                "Offer successfully updated";
        send(stompClientSeller, compositeDisposableSeller, "/swipr/updateOffer", offer.generateQuery(), TAG1);
        assertionWrapper(expectedResponsePayload, sellerUpdate.getBuffer(), TAG1);

        String TAG2 = "findOffer";
        offer.endTime = ldtTimePlus1;
        offer.userId = buyerId;
        send(stompClientBuyer, compositeDisposableBuyer, "/swipr/findOffers", offer.generateQuery(), TAG2);
        String payload2 = buyerFind.getBuffer();
        JSONObject payloadJSONObject2 = new JSONObject();
        long offerId = 1;
        try {
            JSONArray payloadJSONArray2 = new JSONArray(payload2);
            payloadJSONObject2 = (JSONObject) payloadJSONArray2.get(0);
            offerId = payloadJSONObject2.getLong("offerId");
        } catch (Exception e) {Log.d("here there everywhere:", e.getMessage());}
        expectedResponsePayload = "[{\"userId\":7,\"timeRangeStart\":"+ Long.valueOf(epochTime)
                + ",\"timeRangeEnd\":" + Long.valueOf(epochTimePlus2) + ",\"priceCents\":420,\"diningHallBitfield\":1,\"offerId\":" + Long.valueOf(offerId) + "}]";
        assertionWrapper(expectedResponsePayload, payload2, TAG2);

        String TAG3 = "showInterest";
        JSONObject request3 = new JSONObject();
        try {
            request3.put("buyerId", buyerId);
            request3.put("sellQuery", payloadJSONObject2);
        } catch(Exception e) {Log.d("here there everywhere:", e.getMessage());}
        send(stompClientBuyer, compositeDisposableBuyer, "/swipr/showInterest", request3.toString(), TAG3);
        expectedResponsePayload = "{\"buyerId\":1304,\"sellQuery\":{\"userId\":7,\"timeRangeStart\":1575180168,\"timeRangeEnd\":1575187368,\"priceCents\":420,\"diningHallBitfield\":1,\"offerId\":28}}";
        assertionWrapper(expectedResponsePayload, sellerInterest.getBuffer(), TAG3);

        String TAG5 = "cancelOffer";
        expectedResponsePayload =
                "Your offer has been cancelled";
        send(stompClientSeller, compositeDisposableSeller, "/swipr/cancelOffer", TAG3);
        assertionWrapper(expectedResponsePayload, sellerCancel.getBuffer(), TAG5);
    }


    @After
    public void lastTest()
    {
        disconnect();
    }

    void init()
    {
        if(init)
            return;
        init = true;
        compositeDisposableBuyer = new CompositeDisposable();
        try {
            stompClientBuyer = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://157.245.235.19:3000/index");
            stompClientBuyer.connect();
        }
        catch(Exception e){
            Log.d("BUYER + STOMP_FAIL ","ERROR connecting to server " + e.getMessage());}
        compositeDisposableSeller = new CompositeDisposable();
        try {
            stompClientSeller = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://157.245.235.19:3000/index");
            stompClientSeller.connect();
        }
        catch(Exception e){Log.d("SELLER + STOMP_FAIL"," ERROR connecting to server " + e.getMessage());}
        connect(stompClientSeller, compositeDisposableSeller, tagSeller + " connection");
        connect(stompClientBuyer, compositeDisposableBuyer, tagBuyer + " connection");
        subscribe(stompClientSeller, compositeDisposableSeller, reply.getTopic(), tagSeller, reply);
        subscribe(stompClientSeller, compositeDisposableSeller, error.getTopic(), tagSeller, error);
        subscribe(stompClientSeller, compositeDisposableSeller, average.getTopic(), tagSeller, average);
        subscribe(stompClientSeller, compositeDisposableSeller, sellerUpdate.getTopic(), tagSeller, sellerUpdate);
        subscribe(stompClientSeller, compositeDisposableSeller, sellerInterest.getTopic(), tagSeller, sellerInterest);
        subscribe(stompClientSeller, compositeDisposableSeller, sellerCancel.getTopic(), tagSeller, sellerCancel);
        subscribe(stompClientBuyer, compositeDisposableBuyer, reply.getTopic(), tagBuyer, reply);
        subscribe(stompClientBuyer, compositeDisposableBuyer, error.getTopic(), tagBuyer, error);
        subscribe(stompClientBuyer, compositeDisposableBuyer, average.getTopic(), tagBuyer, average);
        subscribe(stompClientBuyer, compositeDisposableBuyer, buyerFind.getTopic(), tagBuyer, buyerFind);
        subscribe(stompClientBuyer, compositeDisposableBuyer, buyerInterest.getTopic(), tagBuyer, buyerInterest);
        try {Thread.sleep(1500);} catch (Exception e){Log.d("here", e.getMessage());}
    }
    void connect(StompClient stompClient, CompositeDisposable compositeDisposable, String tag)
    {
        Disposable dispLifecycle = stompClient.lifecycle()
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d(tag + " Success","Stomp connection opened");
                            break;
                        case ERROR:
                            Log.d(tag + "Stomp connection error",lifecycleEvent.getException().toString());
                            break;
                        case CLOSED:
                            Log.d(tag +" Close","Stomp connection closed");
                            break;
                    }
                });
        compositeDisposable.add(dispLifecycle);
    }
    void disconnect()
    {
        if(init) {
            if (stompClientSeller != null)
                stompClientSeller.disconnect();
            if (compositeDisposableSeller != null) {
                compositeDisposableSeller.dispose();
            }
            if (stompClientBuyer != null)
                stompClientBuyer.disconnect();
            if (compositeDisposableBuyer != null) {
                compositeDisposableBuyer.dispose();
            }
        }
        init = false;
    }
    void subscribe(StompClient stompClient, CompositeDisposable compositeDisposable, String topic, String tag, NetworkResponder responder)
    {
        Disposable dispTopic = stompClient.topic(topic)
                .subscribeOn(Schedulers.io())
                .subscribe(topicMessage -> {
                    Log.d(tag  + " SubSuccess","Received " + topicMessage.getPayload());
                    responder.onMessageReceived(topicMessage.getPayload());
                }, throwable -> {
                    Log.d(tag + " SubFail","Error on subscribe topic" + throwable);
                });

        compositeDisposable.add(dispTopic);
    }
    void send(StompClient stompClient, CompositeDisposable compositeDisposable, String endPoint, String payload, String tag)
    {
        compositeDisposable.add(stompClient.send(endPoint, payload)
                .subscribe(() -> {
                    Log.d(tag + " SendSuccess","STOMP echo send successfully (has payload)");
                }, throwable -> {
                    Log.d(tag + " SendFail","Error send STOMP echo (has payload)" + throwable);
                }));
    }
    void send(StompClient stompClient, CompositeDisposable compositeDisposable, String endPoint, String tag)
    {
        compositeDisposable.add(stompClient.send(endPoint)
                .subscribe(() -> {
                    Log.d(tag + " SendSuccess","STOMP echo send successfully (no payload)");
                }, throwable -> {
                    Log.d(tag + " SendFail","Error send STOMP echo (no payload)" + throwable);
                }));
    }
    void assertionWrapper(String expected, String actual, String tag) throws AssertionError
    {
        try{
            assertEquals(expected, actual);
            Log.d("\n" + resultsTag + " " + tag +  " Success:" + bar, "\n\tExpected:"+expected+"\n\tActual:"+actual);
        } catch (AssertionError e)
        {
            Log.d("\n" + resultsTag + " " + tag + " Failure:" + bar, "\n\tExpected:"+expected+"\n\tActual:"+actual);
            disconnect();
            assertTrue(false);
        }
    }
}