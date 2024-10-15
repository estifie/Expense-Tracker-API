package com.estifie.expensetracker.service;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;

public class HealthServiceImpl implements HealthService {
    @Autowired
    private DataSource dataSource;

    public boolean checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1000);
        } catch (Exception e) {
            return false;
        }
    }
}
