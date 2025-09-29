package com.backend.movies.services;

import com.backend.movies.dto.UserDto;
import com.backend.movies.dto.UserUpdateDTO;
import com.backend.movies.models.UserInfo;
import com.backend.movies.repositories.UserRepository;
import com.backend.movies.utils.ApplicationRole;
import jakarta.validation.Valid;
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
    /**
     * Constructor que inyecta las dependencias necesarias para el servicio.
     * @param repository
     * @param encoder
     */
    @Autowired
    public UserInfoService(UserRepository repository, PasswordEncoder encoder){
        this.repository = repository;
        this.encoder = encoder;
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
    public String deleteUserById(Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return "User deleted successfully";
        }
        throw new IllegalArgumentException("No existe el usuario con el id: " + id);
    }

    /**
     * Método para actualizar la información de un usuario existente.
     * @param id
     * @param dto
     * @return
     */
    public String updateUser(Long id, @Valid UserUpdateDTO dto) {
        Optional<UserInfo> userInfoOptional = repository.findById(id);
        if(userInfoOptional.isEmpty()){
            throw new IllegalArgumentException("User doesn't exist");
        }
        UserInfo userInfo = userInfoOptional.get();
        if(dto.getName() != null){
            userInfo.setName(dto.getName());
        }
        if(dto.getPassword() != null){
            userInfo.setPassword(dto.getPassword());
        }
        if(dto.getAddress() != null){
            userInfo.setAddress(dto.getAddress());
        }
        if(dto.getEmail() != null){
            userInfo.setEmail(dto.getEmail());
        }
        if(dto.getPhoneNumber() != null){
            userInfo.setPhone_number(dto.getPhoneNumber());
        }
        repository.save(userInfo);
        return "User updated successfully";
    }

    /**
     * Método para obtener un usuario por su ID y devolver un UserDto.
     * @param id
     * @return
     */
    public UserDto getUserById(Long id) {
        Optional<UserInfo> userInfoOptional = repository.findById(id);
        if(userInfoOptional.isEmpty()){
            throw new IllegalArgumentException("User doesn't exist");
        }
        UserInfo userInfo = userInfoOptional.get();
        return UserDto.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .address(userInfo.getAddress())
                .phoneNumber(userInfo.getPhone_number())
                .name(userInfo.getName())
                .build();
    }
}
