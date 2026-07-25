package com.ara.dao.impl;

import com.ara.config.DatabaseConnection;
import com.ara.dao.AccountDAO;
import java.sql.*;

public class AccountDAOImpl implements AccountDAO {
    @Override
    public double getBalance(String acc) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement s = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ?")) {
            s.setString(1, acc);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean deposit(String acc, double amt) { return updateBalance(acc, amt); }

    @Override
    public boolean withdraw(String acc, double amt) { return getBalance(acc) >= amt && updateBalance(acc, -amt); }

    @Override
    public boolean transfer(String from, String to, double amt) {
        if (getBalance(from) < amt) return false;
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            updateDb(connection, from, -amt); updateDb(connection, to, amt);
            connection.commit(); return true;
        } catch (Exception e) { return false; }
    }

    private boolean updateBalance(String account, double amount) {
        try (Connection c = DatabaseConnection.getConnection()) { return updateDb(c, account, amount); }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateDb(Connection c, String email, double amt) throws SQLException {
        PreparedStatement statement1 = c.prepareStatement("SELECT accounts.account_id, users.email FROM accounts INNER JOIN users ON accounts.user_id = users.user_id WHERE users.email = ?");
        statement1.setString(1, email);
        ResultSet rs = statement1.executeQuery();
        if (!rs.next()) return false;
        long id = rs.getLong(1);

        PreparedStatement statement2 = c.prepareStatement("UPDATE accounts AS a SET balance = balance + ? INNER JOIN users AS u WHERE a.user_id = u.users_id WHERE users.email = ?");
        statement2.setDouble(1, amt);
        statement2.setString(2,email);
        statement2.executeUpdate();

        PreparedStatement statement3 = c.prepareStatement("INSERT INTO transactions (account_id, type, amount, description) VALUES (?, ?, ?, ?)");
        statement3.setLong(1, id);
        statement3.setString(2, amt > 0 ? "Deposit" : "Withdrawal");
        statement3.setDouble(3, Math.abs(amt));
        statement3.setString(4, "System Transaction");
        statement3.executeUpdate(); return true;
    }

    @Override
    public String getAccountName(String username) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement s = connection.prepareStatement("SELECT first_name FROM users WHERE email = ?")) {
            s.setString(1, username);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }
}