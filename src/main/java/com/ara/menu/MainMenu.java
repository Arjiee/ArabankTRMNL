package com.ara.menu;

import com.ara.model.HandleLogin;
import com.ara.util.InputUtil;
import com.ara.model.HandleRegistration;


public class MainMenu {


    private final HandleLogin handleLogin;
    private final HandleRegistration handleRegistration;

    public MainMenu() {
        // 2. Initialize them when MainMenu starts
        this.handleLogin = new HandleLogin();
        this.handleRegistration = new HandleRegistration();
    }

    public void start() {
        boolean running = true;

        while (running) {
            displayMenu();

            // Get the choice using your utility
            String choice = InputUtil.getString("Choose an option: ");

            switch(choice) {
                case "1":
                    // 3. Call the method on your INSTANCE variable (lowercase 'h')
                    // Change ".execute()" to whatever you named the method inside your HandleLogin file
                    handleLogin.handleLogin();
                    break;
                case "2":
                    // Call the method on your INSTANCE variable
                    // Change ".execute()" to whatever you named the method inside your HandleRegistration file
                    handleRegistration.handleRegistration();
                    break;
                case "3":
                    System.out.println("Thank you for using AraBank. Goodbye!");
                    running = false; // Breaks the while loop
                    break;
                default:
                    System.out.println("[ERROR] Invalid option. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n----------------------------------");
        System.out.println("      WELCOME TO ARABANK");
        System.out.println("----------------------------------");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }
}