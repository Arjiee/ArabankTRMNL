package com.ara.service;

import com.ara.dao.AccountDAO;
import com.ara.dao.impl.AccountDAOImpl;

public class AccountService {
    private final AccountDAO dao = new AccountDAOImpl();

    public double getBalance(String acc) { return dao.getBalance(acc); }

    public boolean deposit(String acc, double amt) { return amt > 0 && dao.deposit(acc, amt); }

    public boolean withdraw(String acc, double amt) { return amt > 0 && dao.withdraw(acc, amt); }

    public boolean transfer(String from, String to, double amt) { return amt > 0 && !from.equals(to) && dao.transfer(from, to, amt); }
}