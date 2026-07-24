package com.ara.model;

public class UserRegistrationDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String email;
    private String phone;
    private String rawPassword;

    // Constructor, Getters, and Setters
    public UserRegistrationDTO(String firstName, String middleName, String lastName, String suffix, String email, String phone, String rawPassword) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.suffix = (suffix == null || suffix.trim().isEmpty() || suffix.equalsIgnoreCase("N/A")) ? "N/A" : suffix.trim();
        this.email = email;
        this.phone = phone;
        this.rawPassword = rawPassword;
    }

    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public String getSuffix() { return suffix; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRawPassword() { return rawPassword; }
}