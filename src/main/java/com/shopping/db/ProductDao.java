package com.shopping.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDao {
    private Logger logger = Logger.getLogger(ProductDao.class.getName());
    Product product = new Product();

    public Product getProduct(int id){
        Connection connection = H2DatabaseConnection.getConnectionToDatabase();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from product where product_id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                product.setQuantity(resultSet.getInt("stock"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "failed to execute getProduct details", e);
        }
        return product;
    }
}
