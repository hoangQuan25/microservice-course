package com.example.accounts.service;

import com.example.accounts.dto.CustomerDetailsDto;

public interface CustomersService {

    CustomerDetailsDto fetchCustomerDetails(String mobileNNumber);
}
