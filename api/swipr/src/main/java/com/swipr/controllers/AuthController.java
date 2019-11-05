package com.swipr.controllers;

import java.util.HashMap;
import java.util.Map;

import com.swipr.auth.AuthenticationFactory;
import com.swipr.auth.Authenticator;
import com.swipr.models.User;
import com.swipr.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@Api(value = "AuthController", description = "Operations pertaining to user creation and login logic")
public class AuthController {
    private static String authorizationRequestBaseUri = "oauth2/authorization";

    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService oauth2AuthorizedClientService;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "Login endpoint", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 202, message = "User logged in", response = User.class),
        @ApiResponse(code = 401, message = "Unable to authenticate user", response = String.class)
    })
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void getLoginPage(Model model) {
        Iterable<ClientRegistration> clientRegistration = null;
        // Iterate through the registered classes
        ResolvableType resolvableType = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);

        if(resolvableType != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(resolvableType.resolveGenerics()[0])) {
            clientRegistration = (Iterable<ClientRegistration>) clientRegistrationRepository;
        } 
        clientRegistration.forEach(registration -> 
            oauth2AuthenticationUrls.put(registration.getClientName(), 
            authorizationRequestBaseUri + "/" + registration.getRegistrationId()));

            model.addAttribute("urls", oauth2AuthenticationUrls);       
    }

    @ApiOperation(value = "The login endpoint redirects to this. Do not directly call this endpoint because it won't work.", response = ResponseEntity.class)
    @RequestMapping(value="/", method = RequestMethod.GET) 
    public ResponseEntity<?> loginInfo(Model model, OAuth2AuthenticationToken token) {
        OAuth2AuthorizedClient client = oauth2AuthorizedClientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());
        String infoUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
        if (!infoUri.isEmpty()) {
            Authenticator authenticator = AuthenticationFactory.getAuthenticator(userRepository, infoUri, client);
            User user = authenticator.authenticate();
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>("Unable to authenticate user", HttpStatus.UNAUTHORIZED);
    } 
    
}