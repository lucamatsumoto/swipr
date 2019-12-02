package com.example.myapplication.Shared;

public class HereBacker {

    private static volatile HereBacker instance;
    private String userJSONString = null;

    private HereBacker() {}

    public static HereBacker getInstance() {
        if (instance == null) {
            synchronized (HereBacker.class) {
                if(instance == null) {
                    instance = new HereBacker();
                }
            }
        }
        return instance;
    }

    // Getter method for accessing the user json
    public String getUserJSONString() {
        return userJSONString;
    }

    // include a setter string for now
    public void setUserJSONString(String userJSONString) {
        this.userJSONString = userJSONString;
    }

}
