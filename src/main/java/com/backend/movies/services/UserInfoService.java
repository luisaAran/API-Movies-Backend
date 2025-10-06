package com.backend.movies.services;

import com.backend.movies.dto.UserUpdateDTO;
import com.backend.movies.exceptions.AuthException;
import com.backend.movies.exceptions.DuplicatedResourceException;
import com.backend.movies.exceptions.ResourceNotFoundException;
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
            throw new AuthException("Credenciales inválidas");
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
            throw new DuplicatedResourceException("Email se encuentra en uso");
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setRole(ApplicationRole.ROLE_USER);
        repository.save(userInfo);
        return "Usuario añadido exitosamente!";
    }

    /**
     * Método para agregar un nuevo usuario con rol de ADMIN.
     * @param userInfo
     * @return
     */
    public String addAdmin(UserInfo userInfo){
        if(repository.existsByEmail(userInfo.getEmail())){
            throw new DuplicatedResourceException("Email se encuentra en uso");
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setRole(ApplicationRole.ROLE_ADMIN);
        repository.save(userInfo);
        return "Admin added successfully";
    }
    public String deleteUserById(Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return "Usuario eliminado exitosamente!";
        }
        throw new ResourceNotFoundException("No existe el usuario");
    }

    /**
     * Método para actualizar la información de un usuario existente.
     * @param id
     * @param dto
     * @return
     */
    public UserInfo updateUser(Long id, @Valid UserUpdateDTO dto) {
        Optional<UserInfo> userInfoOptional = repository.findById(id);
        if(userInfoOptional.isEmpty()){
            throw new ResourceNotFoundException("No existe el usuario");
        }
        UserInfo userInfo = userInfoOptional.get();
        if(dto.getName() != null){
            userInfo.setName(dto.getName());
        }
        if(dto.getPassword() != null){
            userInfo.setPassword(encoder.encode(dto.getPassword()));
        }
        if(dto.getAddress() != null){
            userInfo.setAddress(dto.getAddress());
        }
        if(dto.getEmail() != null){
            if(repository.existsByEmail(dto.getEmail()) &&
                    !userInfo.getEmail().equals(dto.getEmail())){
                throw new DuplicatedResourceException("El email ya se encuentra en uso");
            }
            userInfo.setEmail(dto.getEmail());
        }
        if(dto.getPhoneNumber() != null){
            userInfo.setPhone_number(dto.getPhoneNumber());
        }
        return repository.save(userInfo);
    }

    /**
     * Método para obtener un usuario por su ID y devolver un UserDto.
     * @param id
     * @return
     */
    public UserInfo getUserById(Long id) {
        Optional<UserInfo> userInfoOptional = repository.findById(id);
        if(userInfoOptional.isEmpty()){
            throw new ResourceNotFoundException("No existe el usuario");
        }
        return  userInfoOptional.get();
    }
}
