package com.ara.menu;

import com.ara.service.AccountService;
import com.ara.util.InputUtil;

public class DashboardMenu {
    private final AccountService accountService = new AccountService();

    public void display(String acc) {
        while (true) {
            System.out.println("\n--- Dashboard (" + acc + ") ---");
            System.out.println("1. Balance\n2. Deposit\n3. Withdraw\n4. Transfer\n5. Logout");
            String choice = InputUtil.getString("Choose: ");

            switch (choice) {
                case "1":
                    System.out.println("Balance: $" + accountService.getBalance(acc));
                    break;
                case "2":
                    System.out.println(accountService.deposit(acc, InputUtil.getDouble("Amount: ")) ? "Success" : "Failed");
                    break;
                case "3":
                    System.out.println(accountService.withdraw(acc, InputUtil.getDouble("Amount: ")) ? "Success" : "Failed");
                    break;
                case "4":
                    System.out.println(accountService.transfer(acc, InputUtil.getString("To Account: "), InputUtil.getDouble("Amount: ")) ? "Success" : "Failed");
                    break;
                case "5":
                    return;
            }
        }
    }
}