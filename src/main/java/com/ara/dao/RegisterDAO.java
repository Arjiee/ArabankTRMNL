package com.ara.dao;

public interface RegisterDAO {
    public String register(String firstName, String middleName, String lastName, String suffix, String email, String phone, String password);

    Integer latestUserId(Integer userId);
}
