package com.product.management.system.productmanagementsystem.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class Utility {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static String EncryptPassword(String str) {
        return BCrypt.hashpw(str, BCrypt.gensalt());
    }

    public static boolean CheckPassword(String pw1, String pw2) {
       return BCrypt.checkpw(pw1, pw2);
    }

    public static Long getUserIdFromSession(HttpSession session) throws UserNotAuthenticatedException {
        if (session.getAttribute("userId") == null) {
            throw new UserNotAuthenticatedException("Please login to perform action!");
        }
        return (Long) session.getAttribute("userId");
    }
}
