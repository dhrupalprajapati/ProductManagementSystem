package com.product.management.system.productmanagementsystem.controller;

import com.product.management.system.productmanagementsystem.dto.UserDTO;
import com.product.management.system.productmanagementsystem.service.UserService;
import com.product.management.system.productmanagementsystem.util.UserNotAuthenticatedException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.product.management.system.productmanagementsystem.util.Utility.getUserIdFromSession;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUser() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            LOGGER.info("getAllUser :: all users fetched successfully!");
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            LOGGER.error("getAllUser :: exception occured while fetching users, ", e);
            return ResponseEntity.internalServerError().body("Something went wrong while fetching all users!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        try {
            userService.saveUser(userDTO);
            LOGGER.info("createUser :: user created succuessfully for userDTO: {}", userDTO);
            return ResponseEntity.ok("User created sucessfully!");
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("createUser :: got DataIntegrityViolationException exception while creating user with userDTO: {}", userDTO, e);
            return ResponseEntity.badRequest().body("Email already exists in database, please provide another email!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody Map<String, String> creds, HttpSession session) {
        try {
            UserDTO userDTO = userService.validateUserWithPassword(creds, session);
            LOGGER.info("userLogin :: user logged in successfully with userDTO: {}", userDTO);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            LOGGER.error("userLogin :: got exception for user login for email: {}", creds.get("email").toString(), e);
            return ResponseEntity.internalServerError().body("Something went wrong while performing user login!");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> userLogout(HttpSession session) {
        Long userId = null;
        try {
            userId = getUserIdFromSession(session);
            session.invalidate();
            LOGGER.info("userLogout :: user logged out successfully for userId: {}", userId);
            return ResponseEntity.ok("User logged out successfully!");
        }  catch (UserNotAuthenticatedException e) {
            LOGGER.error("userLogout :: got UserNotAuthenticatedException", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("userLogout :: got exception for user logout for usrId: {}", userId, e);
            return ResponseEntity.internalServerError().body("Something went wrong while performing user logout!");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO, HttpSession session) {
        Long userId = null;
        try {
            userId = getUserIdFromSession(session);
            userService.updateUser(userDTO, userId);
            LOGGER.info("updateUser :: user updated succuessfully for userDTO: {} for userId: {}", userDTO, userId);
            return ResponseEntity.ok("User updated sucessfully!");
        } catch (UserNotAuthenticatedException e) {
            LOGGER.error("updateUser :: got UserNotAuthenticatedException", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("updateUser :: got DataIntegrityViolationException for requested userDTO: {}, userId: {}", userDTO, userId, e);
            return ResponseEntity.badRequest().body("Email already exists in database, please provide another email!");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(HttpSession session) {
        Long userId = null;
        try {
            userId = getUserIdFromSession(session);
            userService.deleteUser(userId);
            LOGGER.info("deleteUser :: user and associted products deleted successfully for userId: {}", userId);
            return ResponseEntity.ok("User and associated products delete successfully!");
        } catch (UserNotAuthenticatedException e) {
            LOGGER.error("deleteUser :: got UserNotAuthenticatedException", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("deleteUser :: got exception while deleting user with userId: {}", userId, e);
            return ResponseEntity.internalServerError().body("Something went wrong while deleting the user!");
        }
    }

}
