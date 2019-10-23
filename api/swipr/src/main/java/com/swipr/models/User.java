package com.swipr.models;

import lombok.*;

// potential user model example

// @Entity we may need this if we use a DB
@Getter @Setter @NoArgsConstructor
public class User {

    private String name;
    private String email;
    private String venmo;

    public User(String name, String email, String venmo) {
        this.name = name;
        this.email = email;
        this.venmo = venmo;
    }
    
}