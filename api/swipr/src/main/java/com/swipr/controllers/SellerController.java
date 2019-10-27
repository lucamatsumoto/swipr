package com.swipr.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@Api(value = "SellerController", description = "Operations pertaining to seller logic")
public class SellerController {

    // @Autowired
    // private UserRepository userRepository;

    // Sample stuff
    @ApiOperation(value = "Test endpoint", response = ResponseEntity.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Seller test working\n", response = String.class)
    })
    @RequestMapping(value="/seller/test", method=RequestMethod.GET) 
    public ResponseEntity<?> testEndpoint() {
        return new ResponseEntity<>("Seller test working\n", HttpStatus.OK);
    }
}




