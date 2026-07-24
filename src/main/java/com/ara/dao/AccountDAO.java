package com.ara.dao;

public interface AccountDAO {
    double getBalance(String accountNumber);

    boolean deposit(String accountNumber, double amount);

    boolean withdraw(String accountNumber, double amount);

    boolean transfer(String fromAccount, String toAccount, double amount);
}