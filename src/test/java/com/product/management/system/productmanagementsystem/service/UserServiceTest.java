package com.product.management.system.productmanagementsystem.service;

import com.product.management.system.productmanagementsystem.domain.User;
import com.product.management.system.productmanagementsystem.dto.UserDTO;
import com.product.management.system.productmanagementsystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void init() {
        user = new User(1L, "dhrupal", "dhrupal@gmail.com", "test_pass", Set.of());
    }

    @Test
    void getAllUsersTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("dhrupal@gmail.com");
        userDTO.setName("dhrupal");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> userDTOS = userService.getAllUsers();

        assertThat(userDTOS).isNotNull();
        assertEquals(userDTOS.size(), 1, "Users are more than one!");
    }

    @Test
    void findUserByIdWhenUserExistsReturnsUserOptionalTest() {
        Long userId = 1L;

        when(userService.findUserById(userId)).thenReturn(Optional.of(user));

        Optional<User> optionalUser = userService.findUserById(userId);

        assertTrue(optionalUser.isPresent());
        assertEquals(user, optionalUser.get(), "User not exists with userId given: " + userId);
    }

    @Test
    void findUserByIdWhenUserExistsReturnsUserOptionalEmptyTest() {
        Long userId = 2L;
        when(userService.findUserById(userId)).thenReturn(Optional.empty());
        Optional<User> optionalUser = userService.findUserById(userId);
        assertEquals(optionalUser, Optional.empty(), "User found for userId: " + userId);
    }

    @Test
    public void saveUserTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("dhrupal@gmail.com");
        userDTO.setPassword("bHjjl5688CF");

        when(userRepository.save(any(User.class))).thenAnswer((Answer<User>) invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        User savedUser = userService.saveUser(userDTO);

        assertEquals(1L, savedUser.getId());
        assertEquals("dhrupal@gmail.com", savedUser.getEmail(), "Email check got failed while user create.");
        assertEquals(userDTO.getPassword(), savedUser.getPassword(), "Password validation got failed while user create.");
    }

    @Test
    public void updateUserTest() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("dhrupal.prajapati@gmail.com");
        userDTO.setPassword("bHjjl5688CF");

        User user = new User();
        user.setId(userId);
        user.setEmail("dhrupal.prajapati@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer((Answer<User>) invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(userDTO, userId);

        assertEquals("dhrupal.prajapati@gmail.com", updatedUser.getEmail(), "Email check got failed while user update.");
        assertEquals(userDTO.getPassword(), updatedUser.getPassword(), "Password validation got failed while user update.");
    }

    @Test
    public void deleteUserTest() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        assertAll(() -> userService.deleteUser(userId));
    }

    @Test
    public void validateUserWithPasswordTest() {
        Map<String, String> creds = new HashMap<>();
        creds.put("email", "dhrupal@gmail.com");
        creds.put("password", "bHjjl5688CF");

        User user = new User();
        user.setId(1L);
        user.setName("dhrupal");
        user.setEmail("dhrupal@gmail.com");
        user.setPassword("$2a$12$kM/.fLCsFXtPbWHBgaTaYuy4rsz3xI9riNmvvb.s177Yuq9OnF7e.");

        when(userRepository.findByEmailEqualsIgnoreCase("dhrupal@gmail.com")).thenReturn(Optional.of(user));

        HttpSession session = mock(HttpSession.class);
        UserDTO userDTO = userService.validateUserWithPassword(creds, session);

        assertEquals("dhrupal@gmail.com", userDTO.getEmail(), "Email check fails while validating user");
        assertEquals("dhrupal", userDTO.getName(), "Name check failed while validating user");
    }

}
