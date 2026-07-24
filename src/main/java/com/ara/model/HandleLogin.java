package com.ara.model;

import com.ara.util.InputUtil;
import com.ara.service.LoginService;    // Import your LoginService
import com.ara.menu.DashboardMenu;      // Import your DashboardMenu

public class HandleLogin {

    private final LoginService loginService;
    private final DashboardMenu dashboardMenu;

    public HandleLogin() {
        this.loginService = new LoginService();
        this.dashboardMenu = new DashboardMenu();
    }

    public void handleLogin() {
        System.out.println("\n----------------------------------");
        System.out.println("      WELCOME TO ARABANK");
        System.out.println("----------------------------------");
        String username = InputUtil.getString("Email/Username: ");
        String password = InputUtil.getString("Password: ");

        if (loginService.login(username, password)) {
            System.out.println("\n[SUCCESS] Login successful.");

            // 4. Use the dashboardMenu variable
            dashboardMenu.display(username);
        } else {
            System.out.println("\n[ERROR] Invalid credentials.");
        }
    }
}