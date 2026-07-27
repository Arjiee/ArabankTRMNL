package com.ara.dao.impl;

import com.ara.config.DatabaseConnection;
import com.ara.dao.UserDAO;
import com.ara.model.UserRegistrationDTO;
import com.ara.util.AccountNumberGenerator;

import java.sql.*;
import java.util.logging.Logger;


public class UserDAOImpl implements UserDAO {

@Override
public boolean authenticateUser(String username, String password) {
    String sql = "SELECT password_hash FROM users WHERE email = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, username);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String storedPassword = rs.getString("password_hash");
                return storedPassword.equals(password);
            }
        }
    } catch (SQLException e) {
        System.err.println("Authentication error: " + e.getMessage());
    }

    return false;
}


@Override
public String registerUser(UserRegistrationDTO userDTO, String hashedPassword) {

    String insertUserSql = "INSERT INTO users (first_name, middle_name, last_name, suffix, email, password_hash, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
    String insertAccountSql = "INSERT INTO accounts (user_id, account_number, account_type, balance, status) VALUES (?, ?, 'Savings', 0.00, 'Active')";
    Connection conn = null;

    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false); // Begin Transaction

            //Insert into Users table
        long generatedUserId = -1;
        try (PreparedStatement userStmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
            userStmt.setString(1, userDTO.getFirstName());
            userStmt.setString(2, userDTO.getMiddleName());
            userStmt.setString(3, userDTO.getLastName());
            userStmt.setString(4, userDTO.getSuffix());
            userStmt.setString(5, userDTO.getEmail());
            userStmt.setString(6, hashedPassword);
            userStmt.setString(7, userDTO.getPhone());
            userStmt.executeUpdate();

            try (ResultSet rs = userStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedUserId = rs.getLong(1);
                }
            }
        }

        if (generatedUserId == -1) {
            conn.rollback();
            return null;
        }

        //Generate Account Number and Insert into Accounts table
        String newAccountNumber = AccountNumberGenerator.accountNumberGenerator(generatedUserId);

        try (PreparedStatement accStmt = conn.prepareStatement(insertAccountSql)) {
            accStmt.setLong(1, generatedUserId);
            accStmt.setString(2, newAccountNumber);
            accStmt.executeUpdate();
        }

        conn.commit();
        return newAccountNumber;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { }
            }
            System.err.println("Registration Error: " + e.getMessage());
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); conn.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}