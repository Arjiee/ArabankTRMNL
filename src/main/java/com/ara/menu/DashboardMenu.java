package com.ara.menu;

import com.ara.service.AccountService;
import com.ara.util.InputUtil;

public class DashboardMenu {
    private final AccountService accountService = new AccountService();
    private String account;
    private String email;

    public void setAccount(String account) {
        this.account = account;

    }


    public void setEmail(String email) {

        this.email = email;
    }

    private void reDisplayMenu(String acc){

        display(acc);
    }

    public void display(String acc) {
        setEmail(acc);
        while (true) {
            String accountName = accountService.getAccountName(acc);
            setAccount(accountName);
            System.out.println("\n-----------------------------------------------------------------------------");
            System.out.println("\n                           " + this.account.toUpperCase() + "'S Dashboard           ");
            System.out.println("\n-----------------------------------------------------------------------------");
            System.out.println("\n                 1.Deposit 2.Withdraw 3.Transfer 4.Logout");
            System.out.println("\n-----------------------------------------------------------------------------");

            System.out.println("\nBalance: $" + accountService.getBalance(acc));

            String choice = InputUtil.getString("Choose: ");

            switch (choice) {

                case "1":
                    System.out.println(accountService.deposit(email, InputUtil.getDouble("Amount: ")) ? "Success" : "Failed");
                    reDisplayMenu(acc);
                    return;
                case "2":
                    System.out.println(accountService.withdraw(email, InputUtil.getDouble("Amount: ")) ? "Success" : "Failed");
                    reDisplayMenu(acc);
                    return;
                case "3":
                    System.out.println(accountService.transfer(email, InputUtil.getString("To Account: "), InputUtil.getDouble("Amount: ")) ? "Success" : "Failed");
                    reDisplayMenu(acc);
                    return;
                case "4":
                    return;
                default:
                    System.out.println("Invalid Input");
                    reDisplayMenu(acc);
                    return;
            }
        }
    }
}