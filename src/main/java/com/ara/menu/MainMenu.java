package com.ara.menu;

import com.ara.model.HandleLogin;
import com.ara.util.InputUtil;
import com.ara.model.HandleRegistration;


public class MainMenu {


    private final HandleLogin handleLogin;
    private final HandleRegistration handleRegistration;

    public MainMenu() {

        this.handleLogin = new HandleLogin();
        this.handleRegistration = new HandleRegistration();
    }

    public void start() {
        boolean running = true;

        while (running) {
            displayMenu();

            String choice = InputUtil.getString("Choose an option: ");

            switch(choice) {
                case "1":
                    handleLogin.handleLogin();
                    break;
                case "2":
                    handleRegistration.handleRegistration();
                    break;
                case "3":
                    System.out.println("Thank you for using AraBank. Goodbye!");
                    running = false;
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