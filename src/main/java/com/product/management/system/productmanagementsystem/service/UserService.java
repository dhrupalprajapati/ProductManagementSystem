package com.product.management.system.productmanagementsystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.product.management.system.productmanagementsystem.domain.User;
import com.product.management.system.productmanagementsystem.dto.UserDTO;
import com.product.management.system.productmanagementsystem.repository.UserRepository;
import com.product.management.system.productmanagementsystem.util.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.product.management.system.productmanagementsystem.util.Utility.CheckPassword;
import static com.product.management.system.productmanagementsystem.util.Utility.objectMapper;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOS = objectMapper.convertValue(userRepository.findAll(), new TypeReference<List<UserDTO>>() {
        });
        userDTOS.forEach(userDTO -> userDTO.setPassword(null));
        return userDTOS;
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User saveUser(UserDTO userDTO) {
        userDTO.setPassword(Utility.EncryptPassword(userDTO.getPassword()));
        return userRepository.save(objectMapper.convertValue(userDTO, User.class));
    }

    public User updateUser(UserDTO userDTO, Long userId) {
        Optional<User> userOptional = findUserById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User doesn't exists with current session!");
        }
        userDTO.setPassword(Utility.EncryptPassword(userDTO.getPassword()));
        User user = objectMapper.convertValue(userDTO, User.class);
        user.setId(userOptional.get().getId());
        return userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmailEqualsIgnoreCase(email);
    }

    public UserDTO validateUserWithPassword(Map<String, String> creds, HttpSession session) {
        Optional<User> userOptional = findUserByEmail(creds.get("email").toString());
        if (!userOptional.isEmpty() && CheckPassword(creds.get("password").toString(), userOptional.get().getPassword())) {
            session.setAttribute("userId", userOptional.get().getId());
            userOptional.get().setPassword(null);
            return objectMapper.convertValue(userOptional.get(), UserDTO.class);
        }
        throw new RuntimeException("Please enter valid email or Password!");
    }

    @Transactional
    @Modifying
    public void deleteUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new RuntimeException("User not found with given userId!");
        }
        userRepository.deleteById(userId);
    }
}
