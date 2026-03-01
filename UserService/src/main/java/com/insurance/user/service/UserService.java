package com.insurance.user.service;

import com.insurance.user.dto.LoginRequest;
import com.insurance.user.dto.LoginResponse;
import com.insurance.user.dto.UserDTO;
import com.insurance.user.entity.User;

import java.util.List;

public interface UserService {
    
    UserDTO registerUser(UserDTO userDTO);
    
    LoginResponse loginUser(LoginRequest loginRequest);
    
    UserDTO getUserById(Long userId);
    
    UserDTO getUserByUsername(String username);
    
    List<UserDTO> getAllUsers();
    
    UserDTO updateUser(Long userId, UserDTO userDTO);
    
    void deleteUser(Long userId);
    
    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
}