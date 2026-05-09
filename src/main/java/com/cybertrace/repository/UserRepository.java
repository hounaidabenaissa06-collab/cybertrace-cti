package com.cybertrace.repository;

import java.util.HashMap;
import java.util.Map;

import com.cybertrace.model.user.User;

public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public void save(User user) {

        users.put(user.getUsername(), user);
    }

    public User findByUsername(String username) {

        return users.get(username);
    }
}
