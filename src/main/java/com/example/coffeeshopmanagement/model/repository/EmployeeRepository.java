package com.example.coffeeshopmanagement.model.repository;

import com.example.coffeeshopmanagement.database.JDBCConnect;
import com.example.coffeeshopmanagement.model.entity.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {
    private final JDBCConnect jdbcConnect;

    public EmployeeRepository() {
        this.jdbcConnect = new JDBCConnect();
    }
    public List<Employee> getAllEmployee(){
        List<Employee> employees = new ArrayList<>();
        try{
            Connection connection = jdbcConnect.getJDBCConnection();
            String select = "SELECT * FROM employee";
            PreparedStatement preparedStatement = connection.prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getInt("employee_id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setJobTitle(resultSet.getString("job_title"));
                employee.setHireDate(resultSet.getDate("hire_date"));
                employee.setSalary(resultSet.getDouble("salary"));
                employees.add(employee);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employees;
    }
    public void addEmployee(Employee employee){
        try {

            Connection connection = jdbcConnect.getJDBCConnection();
            String add = "INSERT INTO employee(first_name, last_name, job_title, hire_date, salary) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(add);
            preparedStatement.setString(1,employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getJobTitle());
            preparedStatement.setString(4, String.valueOf(employee.getHireDate()));
            preparedStatement.setDouble(5, employee.getSalary());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int employeeId){
        String sql = "DELETE FROM employee WHERE employee_id = ? ";
        Connection connection = jdbcConnect.getJDBCConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,employeeId);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee employee) {
        try {
            Connection connection = jdbcConnect.getJDBCConnection();
            String updateSql = "UPDATE employee SET first_name = ?, last_name = ?, job_title = ?, hire_date = ?, salary = ? WHERE employee_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getJobTitle());
            preparedStatement.setString(4, String.valueOf(employee.getHireDate()));
            preparedStatement.setDouble(5, employee.getSalary());
            preparedStatement.setInt(6, employee.getEmployeeId()); // Assuming you have an employee_id field.

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
