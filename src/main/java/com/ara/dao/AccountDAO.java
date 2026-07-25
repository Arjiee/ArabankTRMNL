package com.ara.dao;

public interface AccountDAO {

    String getAccountName(String username);

    double getBalance(String accountNumber);

    boolean deposit(String email, double amount);

    boolean withdraw(String accountNumber, double amount);

    boolean transfer(String fromAccount, String toAccount, double amount);
}