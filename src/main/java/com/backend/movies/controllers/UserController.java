package com.backend.movies.controllers;

import com.backend.movies.dto.AuthRequest;
import com.backend.movies.dto.UserDto;
import com.backend.movies.dto.UserUpdateDTO;
import com.backend.movies.models.UserInfo;
import com.backend.movies.services.JwtService;
import com.backend.movies.services.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: LuisaAran
 * Controlador para la gestión de usuarios y autenticación con
 * JWT.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserInfoService userInfoService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    /**
     * Constructor del Controlador, que inyecta las dependencias necesarias
     * @param userInfoService
     * @param authenticationManager
     * @param jwtService
     */
    public UserController(UserInfoService userInfoService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userInfoService = userInfoService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint para obtener un usuario por su ID.
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id){
        return this.userInfoService.getUserById(id);
    }
    /**
     * Endpoint para la creación de usuarios. Valida la petición y
     * llama a la capa de servicio para añadir el usuario a la base de
     * datos.
     * @param userInfo
     * @return
     */
    @CrossOrigin
    @PostMapping
    public String createUser(@Valid @RequestBody UserInfo userInfo){
        return this.userInfoService.addUser(userInfo);
    }

    /**
     * Endpoint para la creacion de administradores.
     * @param userInfo
     * @return
     */
    @PostMapping("/admin")
    public String createAdmin(@Valid @RequestBody UserInfo userInfo){
        return this.userInfoService.addAdmin(userInfo);
    }

    /**
     * Endpoint para la auntenticación de usuarios. Valida la petición
     * y, si es correcta, llama al servicio JWT para generar un nuevo token
     * @param authRequest
     * @return
     */
    @PostMapping("/generateToken")
    public String authAndGetToken(@Valid @RequestBody AuthRequest authRequest){
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

    /**
     * Endpoint para la actualización de usuarios.
     * Valida la peticion a traves del DTO y llama a la capa de servicio
     * @param id
     * @param userUpdateDTO
     * @return
     */
    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO){
        return this.userInfoService.updateUser(id, userUpdateDTO);
    }

    /**
     * Endpoint para la eliminación de usuarios.
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id){
        return this.userInfoService.deleteUserById(id);
    }
}
