package com.estifie.expensetracker.response.user;

import com.estifie.expensetracker.model.User;

import java.util.List;

public class UsersResponse {
    public List<User> users;
    
    public UsersResponse(List<User> users) {
        this.users = users;
    }
}
