package com.ara.dao.impl;

import com.ara.config.DatabaseConnection;
import com.ara.dao.AccountDAO;
import java.sql.*;

public class AccountDAOImpl implements AccountDAO {


    @Override
    public double getBalance(String email) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement s = connection.prepareStatement("SELECT a.balance, u.email FROM accounts AS a INNER JOIN users AS u ON a.user_id = u.user_id WHERE u.email = ?")) {
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean deposit(String email, double amount) {
        try {
            return depositAmount(email, amount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean withdraw(String email, double amount) {
        try {
           return withdrawAmount(email,amount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean transfer(String from, String to, double amt) {
        if (getBalance(from) < amt) return false;
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            updateDb(connection, from, -amt); updateDb(connection, to, amt);
            connection.commit(); return true;
        } catch (Exception e) { return false; }
    }


    private long getAccountID(String email) {

        long id = 0;

        try( Connection c = DatabaseConnection.getConnection();
            PreparedStatement stmAccount = c.prepareStatement("SELECT a.account_id, u.email FROM accounts AS a INNER JOIN users AS u ON a.user_id = u.user_id WHERE u.email = ?")) {

                stmAccount.setString(1, email);
                ResultSet  rsId = stmAccount.executeQuery();

                if(!rsId.next()) return 0;

                id = rsId.getLong(1);

        }catch (SQLException e){
            e.printStackTrace();

        }
        return id;
    }

    private double getCurrentBalance(String email) {

        long id = getAccountID(email);
        double currentAmount;

        try ( Connection c = DatabaseConnection.getConnection();
              PreparedStatement stmGetCurrentAmount = c.prepareStatement("SELECT balance FROM accounts WHERE account_id = ?")) {

            stmGetCurrentAmount.setLong(1, id);
            ResultSet rsBalance = stmGetCurrentAmount.executeQuery();
            if (!rsBalance.next()) return -1;

            currentAmount = rsBalance.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;

        }
        return currentAmount;
    }

    private boolean withdrawAmount(String email, double amount) throws SQLException{
        long accountID = getAccountID(email);
        double currentAmount = getCurrentBalance(email);
        double newAmount;

        newAmount = currentAmount-amount;

        if (newAmount < 0 ) return false;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmUpdateBalance = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE account_id = ?")){

            stmUpdateBalance.setDouble(1, newAmount);
            stmUpdateBalance.setLong(2, accountID);
            stmUpdateBalance.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean depositAmount(String email, double amount) throws SQLException {
        double currentAmount = getCurrentBalance(email);
        long accountID = getAccountID(email);

        double newAmount;

        if (amount<=0 && accountID == 0 ){
            return false;
        }
        if (currentAmount == -1) return false;

        //add balances then save as new amount
        newAmount = currentAmount + amount;

        //update the database balance with the new amount
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmUpdateBal = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE account_id = ?")) {

            stmUpdateBal.setDouble(1, newAmount);
            stmUpdateBal.setLong(2,accountID);

            stmUpdateBal.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean updateDb(Connection c, String email, double amt) throws SQLException {
        PreparedStatement statement1 = c.prepareStatement("SELECT accounts.account_id, users.email FROM accounts INNER JOIN users ON accounts.user_id = users.user_id WHERE users.email = ?");
        statement1.setString(1, email);
        ResultSet rs = statement1.executeQuery();
        if (!rs.next()) return false;
        long id = rs.getLong(1);

        PreparedStatement statement2 = c.prepareStatement("UPDATE accounts AS a INNER JOIN users AS u ON a.user_id = u.user_id SET a.balance = ? WHERE u.email = ?");
        statement2.setDouble(1, amt);
        statement2.setString(2,email);
        statement2.executeUpdate();

        PreparedStatement statement3 = c.prepareStatement("INSERT INTO transactions (account_id, type, amount, description) VALUES (?, ?, ?, ?)");
        statement3.setLong(1, id);
        statement3.setString(2, amt > 0 ? "Deposit" : "Withdrawal");
        statement3.setDouble(3, Math.abs(amt));
        statement3.setString(4, "System Transaction");
        statement3.executeUpdate();
        return true;
    }

    @Override
    public String getAccountName(String email) {
        String username = "";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement s = connection.prepareStatement("SELECT first_name FROM users WHERE email = ?")) {
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                username = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return username;
    }
}