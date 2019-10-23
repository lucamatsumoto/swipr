package com.swipr.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class BuyerController {

    // Sample stuff
    @RequestMapping(value="/buyer/test", method=RequestMethod.GET) 
    public ResponseEntity<?> testEndpoint() {
        return new ResponseEntity<>("Buyer test working\n", HttpStatus.OK);
    }
}




