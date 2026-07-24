package com.ara.service;

import com.ara.dao.UserDAO;
import com.ara.dao.impl.UserDAOImpl;
import com.ara.util.SecurityUtil;

public class LoginService {
    private final UserDAO userDAO;

    public LoginService() {
        this.userDAO = new UserDAOImpl();
    }

    public boolean login(String username, String rawPassword) {
        // Hash the password the user typed
        String hashedPassword = SecurityUtil.hashPassword(rawPassword);

        // Call the method exactly as it is spelled in the interface
        return userDAO.authenticateUser(username, hashedPassword);
    }
}