package com.ara.dao.impl;

import com.ara.config.DatabaseConnection;
import com.ara.dao.RegisterDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class RegisterDAOImpl implements RegisterDAO {



    @Override
    public String register(String firstName, String middleName, String lastName, String suffix, String email, String phone, String password) {
        String accNum = String.valueOf(10000000 + new Random().nextInt(90000000));

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement uStmt = conn.prepareStatement("INSERT INTO users (first_name, middle_name, last_name, suffix, email, password_hash, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                uStmt.setString(1, firstName);
                uStmt.setString(2, middleName);
                uStmt.setString(3, lastName);
                uStmt.setString(4, suffix);
                uStmt.setString(5, email);
                uStmt.setString(6, password);
                uStmt.setString(7, phone);
                uStmt.executeUpdate();
                ResultSet rs = uStmt.getGeneratedKeys();
                if (rs.next()) {
                    long userId = rs.getLong(1);
                    try (PreparedStatement aStmt = conn.prepareStatement("INSERT INTO accounts (user_id, account_number, account_type, balance, status) VALUES (?, ?, 'Savings', 0, 'Active')")) {
                        aStmt.setLong(1, userId); aStmt.setString(2, accNum);
                        aStmt.executeUpdate();
                    }
                }
            }
            conn.commit();
            return accNum;
        } catch (Exception e) {
            return null;
        }
    }

}
