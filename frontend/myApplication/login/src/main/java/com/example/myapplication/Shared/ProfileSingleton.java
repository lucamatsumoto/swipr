package com.example.myapplication.Shared;

public class ProfileSingleton {
    private static ProfileSingleton instance;
    private ProfileSingleton(){}
    public static ProfileSingleton getInstance(){
        if(instance == null)
            instance = new ProfileSingleton();
        return instance;
    }
    public static void setInstance(String json){
        //TODO: parse json, getters and setters
    }
    private int ID;
    private String firstName;
    private String lastName;
    private String email;
    private String venmo;
    private boolean here;
    private boolean matchedOffer;
}
