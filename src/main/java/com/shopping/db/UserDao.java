package com.shopping.db;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    // method to get user details
    public User getDetails(String username) {
        User user = new User();
        try{
            Connection connection = H2DatabaseConnection.getConnectionToDatabase();
            PreparedStatement query = null;
            query = connection.prepareStatement("select * from user where username=?");
            query.setString(1, username);
            ResultSet res = query.executeQuery();
            while(res.next()){
                user.setId(res.getInt("id"));
                user.setName(res.getString("name"));
                user.setUsername(res.getString("username"));
                user.setAge(res.getInt("age"));
                user.setGender(res.getString("gender"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to execute getUser query", e);
        }
        return user;
    }
}
