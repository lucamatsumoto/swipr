package com.swipr.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User superclass that represents the provided user information from Google, and their venmo account. 
 * This information is stored in our PostgreSQL database.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// Might wanna set up a listener later but it's okay for now
@Data
@Table(name="Users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id; // Will serve as the primary key in our SQL DB

    @NotBlank(message = "first name is mandatory")
    private String firstName;
    
    @NotBlank(message = "last name is mandatory")
    private String lastName;

    @NotBlank(message = "email is mandatory")
    private String email;

    private String venmo;

    // Mark some properties with @transient so that they aren't stored in the DB
    @Transient
    @JsonIgnore
    private boolean here;

    @Transient
    @JsonIgnore
    private boolean matchedOffer;

    @Transient
    @JsonIgnore
    private Set<String> preferredDiningHalls;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.here = false;
        this.matchedOffer = false;
        this.preferredDiningHalls = new HashSet<>();
    }

    /**
     * Helper method for adding/updating the preferred dining hall of the user
     * @param diningHall dininghall that the user is looking for
     */
    public void addPreferredDiningHall(String diningHall) {
        preferredDiningHalls.add(diningHall);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        System.out.println(user.id);
        System.out.println(this.id);
        return user.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        int result = 3;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}