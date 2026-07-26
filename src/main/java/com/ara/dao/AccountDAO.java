package com.ara.dao;

public interface AccountDAO {

    String getAccountName(String email);

    double getBalance(String email);

    boolean deposit(String email, double amount);

    boolean withdraw(String email, double amount);

    boolean transfer(String fromAccount, String toAccount, double amount);
}