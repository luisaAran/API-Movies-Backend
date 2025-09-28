package com.backend.movies.services;

import com.backend.movies.models.UserInfo;
import com.backend.movies.repositories.UserRepository;
import com.backend.movies.utils.ApplicationRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserInfoService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    @Autowired
    public UserInfoService(UserRepository repository, PasswordEncoder encoder){
        this.repository = repository;
        this.encoder = encoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<UserInfo> user = repository.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        UserInfo userInfo = user.get();
        return new UserInfoDetails(userInfo);
    }
    public String addUser(UserInfo userInfo){
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setRole(ApplicationRole.ROLE_USER);
        repository.save(userInfo);
        return "User added successfully";
    }
    public String addAdmin(UserInfo userInfo){
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setRole(ApplicationRole.ROLE_ADMIN);
        repository.save(userInfo);
        return "Admin added successfully";
    }
}
