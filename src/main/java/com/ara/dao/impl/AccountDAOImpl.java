package com.ara.dao.impl;

import com.ara.config.DatabaseConnection;
import com.ara.dao.AccountDAO;
import java.sql.*;
import java.util.Objects;

public class AccountDAOImpl implements AccountDAO {


    @Override
    public double getBalance(String email) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement s = connection.prepareStatement("SELECT a.balance, u.email FROM accounts AS a INNER JOIN users AS u ON a.user_id = u.user_id WHERE u.email = ?")) {
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException();
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
            throw new RuntimeException();
        }
    }

    @Override
    public boolean transfer(String from, String to, double amount) {

        String senderAccNum = getAccountNumberByEmail(from);
        double senderBalance = getBalanceByEmail(from);
        double receiverBalance = getBalanceByAccNumber(to);


        double senderNewBalance = senderBalance - amount;
        double receiverNewBalance = receiverBalance + amount;

        if ( senderBalance < amount) return false;
        if (Objects.equals(senderAccNum, to)) {
            System.out.println("Cannot transfer to yourself");
            return false;
        } else if (getBalanceByAccNumber(to) == -1) {
            System.out.println("Acount doesn't exist!");
            return false;
        }


        //update sender new balance
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement updateSenderBalancesSQL = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE account_number = ?")){

            conn.setAutoCommit(false); //hold all queries, do not commit upon execution

            updateSenderBalancesSQL.setDouble(1, senderNewBalance);
            updateSenderBalancesSQL.setString(2, senderAccNum);
            updateSenderBalancesSQL.executeUpdate();

                //update receiver new balance
                try(PreparedStatement updateReceiverBalanceSQL = conn.prepareStatement("UPDATE accounts SET balance = ? WHERE account_number = ?")){

                    updateReceiverBalanceSQL.setDouble(1,receiverNewBalance);
                    updateReceiverBalanceSQL.setString(2,to);
                    updateReceiverBalanceSQL.executeUpdate();

                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }

                //insert info into transfer_transactions table
                try(PreparedStatement insertToTransfer = conn.prepareStatement("INSERT INTO transfer_transactions(transfer_from,transfer_to,amount) VALUES (?,?,?)")){

                    insertToTransfer.setString(1,senderAccNum);
                    insertToTransfer.setString(2,to);
                    insertToTransfer.setDouble(3, amount);
                    insertToTransfer.executeUpdate();
                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }

            conn.commit(); // commit queries


        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    private long getAccountIdByEmail(String email) {

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

    private String getAccountNumberByEmail(String email){
        String accountNumber;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement getAccNumSQL = conn.prepareStatement("SELECT a.account_number,u.email FROM accounts AS a INNER JOIN users AS u ON a.user_id = u.user_id WHERE u.email =?")){

            getAccNumSQL.setString(1, email);
            ResultSet rs = getAccNumSQL.executeQuery();

            if (!rs.next()) return null;

            accountNumber = rs.getString(1);


        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

        return accountNumber;
    }

    private double getBalanceByAccNumber(String accountNumber){

        double balance = -1;

        try( Connection c = DatabaseConnection.getConnection();
             PreparedStatement stmAccount = c.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? ")) {

            stmAccount.setString(1, accountNumber);
            ResultSet  rsBalance = stmAccount.executeQuery();

            if(!rsBalance.next()) return -1;

            balance = rsBalance.getDouble(1);

        }catch (SQLException e){
            e.printStackTrace();

        }
        return balance;
    }

    private double getBalanceByEmail(String email) {

        long id = getAccountIdByEmail(email);
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

    private boolean updateBalanceByAccID(long accountID, double newAmount) {
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

    private boolean withdrawAmount(String email, double amount) throws SQLException{
        long accountID = getAccountIdByEmail(email);
        double currentAmount = getBalanceByEmail(email);
        double newAmount;

        newAmount = currentAmount-amount;

        if (newAmount < 0 ) return false;

        return updateBalanceByAccID(accountID, newAmount);
    }

    private boolean depositAmount(String email, double amount) throws SQLException {
        double currentAmount = getBalanceByEmail(email);
        long accountID = getAccountIdByEmail(email);

        double newAmount;

        if (amount<=0 && accountID == 0 ){
            return false;
        }
        if (currentAmount == -1) return false;

        //add balances then save as new amount
        newAmount = currentAmount + amount;

        //update the database balance with the new amount
        return updateBalanceByAccID(accountID, newAmount);
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