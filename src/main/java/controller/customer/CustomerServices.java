package controller.customer;

import javafx.collections.ObservableList;
import model.Customer;

public interface CustomerServices {
    boolean addCustomer(Customer customer);
    Customer searchCustomer(String customerID);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(String customerID);
    ObservableList<Customer> getAllCustomers();
}
