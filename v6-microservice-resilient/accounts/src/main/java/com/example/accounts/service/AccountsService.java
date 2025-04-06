package com.example.accounts.service;

import com.example.accounts.dto.CustomerDto;

public interface AccountsService {

    /**
     * Creates a new account for the given customer details.
     *
     * @param customerDto the customer details including name, email, and mobile number
     */
    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);
}
