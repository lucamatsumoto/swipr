package com.swipr.models;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter 
@Setter 
@NoArgsConstructor
public class Offer {

    private long id;
    // Thread safe unique id generation 
    private static AtomicLong next_id = new AtomicLong();
    private int cents;
    // private long timestamp;
    private String diningHall;
    private User user;
    private long startTime;
    private long endTime;

    public Offer(String diningHall, User user, long startTime, long endTime) {
        this.cents = 0;
        this.id = next_id.incrementAndGet();
        // this.timestamp = timestamp;
        this.diningHall = diningHall;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Offer)) {
            return false;
        }
        // Check if the user equals ID is the same
        return this.user.getId().equals(((Offer)o).user.getId());
    }

    @Override
    public int hashCode() {
        return user.getId();
    }
}