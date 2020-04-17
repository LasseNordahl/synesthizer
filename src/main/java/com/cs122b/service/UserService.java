package com.cs122b.service;

import com.cs122b.client.SQLClient;
import com.cs122b.client.Query;
import com.cs122b.model.User;

import java.sql.*;

public class UserService {
    private static SQLClient db;

    private static void setUserAttrs(User user, ResultSet result) throws SQLException {
        user.setId(result.getInt("id"));
        user.setFirst_name(result.getString("first_name"));
        user.setLast_name(result.getString("last_name"));
        user.setAddress(result.getString("address"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
    }

    public static User authenticateUser(String email, String password) throws SQLException {
        db = new SQLClient();

        Query query = db
                .query(String.format("SELECT *  FROM user WHERE email='%s' AND password='%s'", email, password));

        User user = new User();
        ResultSet result = query.getResult();

        if (result.next() == false) {
            return null;
        }

        setUserAttrs(user, result);

        query.closeQuery();
        db.closeConnection();
        return user;
    }
}