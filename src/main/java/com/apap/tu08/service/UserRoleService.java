package com.apap.tu08.service;

import com.apap.tu08.model.FlightModel;
import com.apap.tu08.model.UserRoleModel;

import java.util.Optional;

/**
 * FlightService
 */
public interface UserRoleService {
    UserRoleModel addUser(UserRoleModel user);
    public String encrypt(String password);
}