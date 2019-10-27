package com.swipr.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter @NoArgsConstructor
public class Offer {

    private int cents;
    private long timestamp;
    private String diningHall;
    private User user;
    private long preferredTime;

    public Offer(long timestamp, String diningHall, User user, long preferredTime) {
        this.cents = 0;
        this.timestamp = timestamp;
        this.diningHall = diningHall;
        this.user = user;
        this.preferredTime = preferredTime;
    }


}