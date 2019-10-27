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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// Might wanna set up a listener later but it's okay for now
@Data
@Table(name="Users")
@NoArgsConstructor
@ApiModel(description = "User model that is stored in Postgres. It is also the superclass of buyer and sellers.", subTypes = {Buyer.class, Seller.class})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer id; // Will serve as the primary key in our SQL DB

    @ApiModelProperty(notes = "The user's first name", required = true)
    @NotBlank(message = "first name is mandatory")
    private String firstName;
    
    @ApiModelProperty(notes = "The user's last name", required = true)
    @NotBlank(message = "last name is mandatory")
    private String lastName;

    @ApiModelProperty(notes = "The user's email", required = true)
    @NotBlank(message = "email is mandatory")
    private String email;

    @ApiModelProperty(notes = "The user's venmo account", required = false)
    private String venmo;

    // Mark some properties with @transient so that they aren't stored in the DB
    @Transient
    @ApiModelProperty(hidden = true)
    private boolean here;

    @Transient
    @ApiModelProperty(hidden = true)
    private boolean matchedOffer;

    @Transient
    @ApiModelProperty(hidden = true)
    private Set<String> preferredDiningHalls;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.here = false;
        this.matchedOffer = false;
        this.preferredDiningHalls = new HashSet<>();
    }

    public void addPreferredDiningHall(String diningHall) {
        preferredDiningHalls.add(diningHall);
    }
}