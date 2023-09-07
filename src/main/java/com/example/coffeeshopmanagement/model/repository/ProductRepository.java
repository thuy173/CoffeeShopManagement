package com.example.coffeeshopmanagement.model.repository;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import com.example.coffeeshopmanagement.model.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final JDBCConnect jdbcConnect;

    public ProductRepository() {
        this.jdbcConnect = new JDBCConnect();
    }

    public void addProduct(Product product) {
        try {
            // Implement logic to save a new product to the database
            // You would typically use JDBC, Hibernate, or an ORM for database operations.
            // Below is a simplified example using JDBC. Replace with your actual implementation.

            Connection connection = jdbcConnect.getJDBCConnection();

            String insertQuery = "INSERT INTO product (product_name, description, category, price,quantity, ingredients, availability) " +
                    "VALUES (?, ?, ?, ?, ?, ?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setString(3, product.getCategory());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setInt(5, product.getQuantity());
            preparedStatement.setString(6, product.getIngredients());
            preparedStatement.setBoolean(7, product.isAvailability());


            preparedStatement.executeUpdate();

            // Close resources
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }

    public void updateProduct(Product updatedProduct) {
        String sql = "UPDATE product SET product_name = ?, description = ?, category = ?, " +
                "price = ?, quantity = ?, ingredients = ?, availability = ? " +
                "WHERE product_id = ?";
        Connection connection = jdbcConnect.getJDBCConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedProduct.getProductName());
            statement.setString(2, updatedProduct.getDescription());
            statement.setString(3, updatedProduct.getCategory());
            statement.setDouble(4, updatedProduct.getPrice());
            statement.setInt(5, updatedProduct.getQuantity());
            statement.setString(6, updatedProduct.getIngredients());
            statement.setBoolean(7, updatedProduct.isAvailability());
            statement.setInt(8, updatedProduct.getProductId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int productId) {
        String sql = "UPDATE product SET availability = false WHERE product_id = ?";
        Connection connection = jdbcConnect.getJDBCConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try {
            // Implement logic to retrieve all products from the database
            // You would typically use JDBC, Hibernate, or an ORM for database operations.
            // Below is a simplified example using JDBC. Replace with your actual implementation.

            Connection connection = jdbcConnect.getJDBCConnection();

            String selectQuery = "SELECT * FROM product WHERE availability = true";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getInt("product_id"));
                product.setProductName(resultSet.getString("product_name"));
                product.setDescription(resultSet.getString("description"));
                product.setCategory(resultSet.getString("category"));
                product.setPrice(resultSet.getDouble("price"));
                product.setQuantity(resultSet.getInt("quantity"));
                product.setIngredients(resultSet.getString("ingredients"));
                product.setAvailability(resultSet.getBoolean("availability"));

                products.add(product);
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

}
