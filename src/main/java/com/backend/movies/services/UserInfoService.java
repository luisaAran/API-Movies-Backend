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

/**
 * @Author: luisaAran
 * Clase que implementa UserDetailsService para cargar los detalles del usuario desde la base de datos.
 * Proporciona métodos para agregar usuarios y administradores.
 */
@Service
public class UserInfoService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    /**
     * Constructor que inyecta las dependencias necesarias para el servicio.
     * @param repository
     * @param encoder
     */
    @Autowired
    public UserInfoService(UserRepository repository, PasswordEncoder encoder, UserRepository userRepository){
        this.repository = repository;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    /**
     * Método que carga los detalles del usuario a partir del nombre de usuario (email).
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<UserInfo> user = repository.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        UserInfo userInfo = user.get();
        return new UserInfoDetails(userInfo);
    }

    /**
     * Método para agregar un nuevo usuario con rol de USER.
     * @param userInfo
     * @return
     */
    public String addUser(UserInfo userInfo){
        /*
         * Esta validación puede ser reemplazada a futuro con estrategias
         * como uso de caché, para evitar consultas repetitivas a la base de datos
         */
        if(repository.existsByEmail(userInfo.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setRole(ApplicationRole.ROLE_USER);
        repository.save(userInfo);
        return "User added successfully";
    }

    /**
     * Método para agregar un nuevo usuario con rol de ADMIN.
     * @param userInfo
     * @return
     */
    public String addAdmin(UserInfo userInfo){
        if(repository.existsByEmail(userInfo.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setRole(ApplicationRole.ROLE_ADMIN);
        repository.save(userInfo);
        return "Admin added successfully";
    }
}
