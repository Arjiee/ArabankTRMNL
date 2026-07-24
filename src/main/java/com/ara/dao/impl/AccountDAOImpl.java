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
        } catch (Exception e) { }
        return 0;
    }

    @Override
    public boolean deposit(String acc, double amt) { return updateBalance(acc, amt); }

    @Override
    public boolean withdraw(String acc, double amt) { return getBalance(acc) >= amt && updateBalance(acc, -amt); }

    @Override
    public boolean transfer(String from, String to, double amt) {
        if (getBalance(from) < amt) return false;
        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            updateDb(c, from, -amt); updateDb(c, to, amt);
            c.commit(); return true;
        } catch (Exception e) { return false; }
    }

    private boolean updateBalance(String acc, double amt) {
        try (Connection c = DatabaseConnection.getConnection()) { return updateDb(c, acc, amt); }
        catch (Exception e) { return false; }
    }

    private boolean updateDb(Connection c, String acc, double amt) throws SQLException {
        PreparedStatement s1 = c.prepareStatement("SELECT account_id FROM accounts WHERE account_number = ?");
        s1.setString(1, acc); ResultSet rs = s1.executeQuery();
        if (!rs.next()) return false;
        long id = rs.getLong(1);

        PreparedStatement s2 = c.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE account_id = ?");
        s2.setDouble(1, amt); s2.setLong(2, id); s2.executeUpdate();

        PreparedStatement s3 = c.prepareStatement("INSERT INTO transactions (account_id, type, amount, description) VALUES (?, ?, ?, ?)");
        s3.setLong(1, id); s3.setString(2, amt > 0 ? "Deposit" : "Withdrawal");
        s3.setDouble(3, Math.abs(amt)); s3.setString(4, "System Transaction");
        s3.executeUpdate(); return true;
    }
}