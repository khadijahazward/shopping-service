package com.shopping.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDao {
    public static Logger logger = Logger.getLogger(Order.class.getName());

    public List<Order> getOrders(int userId) {
        List<Order> orders = new ArrayList<>();

        try{
            Connection connection = H2DatabaseConnection.getConnectionToDatabase();
            PreparedStatement query = connection.prepareStatement("select * from orders where user_id=?");
            query.setInt(1, userId);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()){
                Order order = new Order();
                order.setOrderDate(resultSet.getDate("order_date"));
                order.setOrderId(resultSet.getInt("order_id"));
                order.setNoOfItems(resultSet.getInt("no_of_items"));
                order.setUserId(resultSet.getInt("user_id"));
                order.setTotalAmount(resultSet.getDouble("total_amount"));
                orders.add(order);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Couldn't execute the getOrders query", e);
        }
        return orders;
    }
}
