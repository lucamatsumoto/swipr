package com.swipr.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SellerController {

    // Sample stuff
    @RequestMapping(value="/seller/test", method=RequestMethod.GET) 
    public ResponseEntity<?> testEndpoint() {
        return new ResponseEntity<>("Seller test working\n", HttpStatus.OK);
    }
}




