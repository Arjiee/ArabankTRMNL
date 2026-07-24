package com.ara.service;

import com.ara.dao.UserDAO;
import com.ara.dao.impl.UserDAOImpl;
import com.ara.model.UserRegistrationDTO;
import com.ara.util.SecurityUtil;

public class RegisterService {

    private final UserDAO userDAO;

    public RegisterService() {
        this.userDAO = new UserDAOImpl();
    }

    public String processRegistration(UserRegistrationDTO dto) {
        String hashedPassword = SecurityUtil.hashPassword(dto.getRawPassword());

        return userDAO.registerUser(dto, hashedPassword);
    }
}