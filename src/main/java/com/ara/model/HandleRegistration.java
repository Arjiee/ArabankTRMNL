package com.ara.model;

import com.ara.service.RegisterService;
import com.ara.util.InputUtil;

public class HandleRegistration {
        public void handleRegistration() {
        System.out.println("\n--- ACCOUNT ENROLLMENT ---");

        String firstName = InputUtil.getString("First Name: ");
        String middleName = InputUtil.getString("Middle Name (Press enter to skip): ");
        String lastName = InputUtil.getString("Last Name: ");
        String suffix = InputUtil.getString("Suffix (e.g., Jr, Sr, III - or leave blank): ");
        String email = InputUtil.getString("Email Address: ");
        String phone = InputUtil.getString("Phone Number: ");
        String password = InputUtil.getString("Create Password: ");

        UserRegistrationDTO registrationData = new UserRegistrationDTO(
                firstName, middleName, lastName, suffix, email, phone, password
        );

        System.out.println("Processing registration...");

        RegisterService registerService = new RegisterService();

        String generatedAccountNumber = registerService.processRegistration(registrationData);

        if (generatedAccountNumber != null) {
            System.out.println("\n[SUCCESS] Registration complete!");
            System.out.println("Your new Account Number is: " + generatedAccountNumber);
        } else {
            System.out.println("\n[ERROR] Registration failed. Email or phone may already exist.");
        }
    }
}

