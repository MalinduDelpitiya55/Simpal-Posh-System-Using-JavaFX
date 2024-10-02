package controller.customer;

import controller.Item.ItemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;
import model.Item;
import util.CrudUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerController implements CustomerServices{

    @Override
    public boolean addCustomer(Customer customer) {
       String SQL = "INSERT INTO customer VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            return CrudUtils.execute(SQL,customer.getId(),customer.getTitle(),customer.getName(),customer.getDob(),customer.getSalary(),customer.getAddress(),customer.getCity(),customer.getProvince(),customer.getPostalCode());

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Customer Added Failed").show();
        }
        return false;
    }

    @Override
    public Customer searchCustomer(String customerID) {

        try {
            ResultSet resultSet = CrudUtils.execute("SELECT * FROM customer WHERE CustID=?", customerID);
            resultSet.next();
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4).toLocalDate(),
                    resultSet.getDouble(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9)
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        String SQL = "UPDATE customer SET CustTitle = ?, CustName = ?, DOB = ?, salary = ?, CustAddress = ?, City = ?, Province = ?, PostalCode = ? WHERE CustID = ?";
        try {
            return CrudUtils.execute(SQL,customer.getTitle(),customer.getName(),customer.getDob(),customer.getSalary(),customer.getAddress(),customer.getCity(),customer.getProvince(),customer.getPostalCode(),customer.getId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,customer.getId()+" Customer Updated Failed").show();
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(String customerID) {
        String SQL = "DELETE FROM customer WHERE CustID = ?";
        try {
            return CrudUtils.execute(SQL,customerID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> observableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = CrudUtils.execute("SELECT * FROM customer");
            while (resultSet.next()) {
                observableList.add(new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4).toLocalDate(),
                        resultSet.getDouble(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                ));
            }
            return observableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<String> getCustomerIds(){
        ObservableList<Customer> allCustomers = getAllCustomers();
        ObservableList<String> idList = FXCollections.observableArrayList();

        allCustomers.forEach(customer->{
            idList.add(customer.getId());
        });

        return idList;
    }
}
