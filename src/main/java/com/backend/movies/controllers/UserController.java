package com.backend.movies.controllers;

import com.backend.movies.models.AuthRequest;
import com.backend.movies.models.UserInfo;
import com.backend.movies.services.JwtService;
import com.backend.movies.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private UserInfoService userInfoService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public UserController(UserInfoService userInfoService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userInfoService = userInfoService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @CrossOrigin
    @PostMapping
    public String createUser(@RequestBody UserInfo userInfo){
        return this.userInfoService.addUser(userInfo);
    }
    @PostMapping("/generateToken")
    public String authAndGetToken(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        if(authentication.isAuthenticated()){
            return this.jwtService.generateToken(authRequest.getUsername());
        }else{
            throw new UsernameNotFoundException("Invalid user Request");
        }
    }
}
