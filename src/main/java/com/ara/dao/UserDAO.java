package com.ara.dao;

import com.ara.model.UserRegistrationDTO;

public interface UserDAO {


    String registerUser(UserRegistrationDTO userDTO, String hashedPassword);

    boolean authenticateUser(String username, String password);

}