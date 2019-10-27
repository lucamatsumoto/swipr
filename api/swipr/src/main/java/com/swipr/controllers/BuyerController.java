package com.swipr.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@Api(value = "BuyerController", description = "Operations pertaining to buyer logic")
public class BuyerController {

    // @Autowired
    // private UserRepository userRepository;

    // Sample stuff
    @ApiOperation(value = "Test endpoint", response = ResponseEntity.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Buyer test working\n", response = String.class)
    })
    @RequestMapping(value="/buyer/test", method=RequestMethod.GET) 
    public ResponseEntity<?> testEndpoint() {
        return new ResponseEntity<>("Buyer test working\n", HttpStatus.OK);
    }

}




