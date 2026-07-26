package com.ara.service;

import com.ara.dao.AccountDAO;
import com.ara.dao.impl.AccountDAOImpl;

public class AccountService {
    private final AccountDAO dao = new AccountDAOImpl();

    public String getAccountName(String email) {return dao.getAccountName(email);}

    public double getBalance(String acc) { return dao.getBalance(acc); }

    public boolean deposit(String acc, double amt) { return amt > 0 && dao.deposit(acc, amt); }

    public boolean withdraw(String email, double amount) { return amount > 0 && dao.withdraw(email, amount); }

    public boolean transfer(String from, String to, double amount) { return amount > 0 && !from.equals(to) && dao.transfer(from, to, amount); }
}