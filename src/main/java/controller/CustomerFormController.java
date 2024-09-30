package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {


    @FXML
    private DatePicker DateBirthDay;

    @FXML
    private JFXButton btnAddCustomer;

    @FXML
    private JFXButton btnAddCustomer1;

    @FXML
    private JFXButton btnAddCustomer11;

    @FXML
    private JFXButton btnAddCustomer12;

    @FXML
    private JFXButton btnReload;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colBirthday;

    @FXML
    private TableColumn<?, ?> colCity;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPostalCode;

    @FXML
    private TableColumn<?, ?> colProvince;

    @FXML
    private TableColumn<?, ?> colSalary;
    @FXML
    private ComboBox <String> combTitle;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableView<Customer> tblCustomer;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPostalCode;

    @FXML
    private JFXTextField txtProvince;

    @FXML
    private JFXTextField txtSalary;





    @FXML
    void ReloadOnAction(ActionEvent event)  {
        loadTable();
    }

    @FXML
    void btnAddCustomerOnAction(ActionEvent event) {
        Customer customer =new Customer(txtID.getText(),combTitle.getValue(),txtName.getText(),DateBirthDay.getValue(),Double.parseDouble(txtSalary.getText()),txtAddress.getText(),txtCity.getText(),txtProvince.getText(),txtPostalCode.getText());
        System.out.println(customer);
        String SQL = "INSERT INTO customer VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/ThogaKade", "root", "root");
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1,customer.getId());
            pstm.setObject(2,customer.getTitle());
            pstm.setObject(3,customer.getName());
            pstm.setObject(4,customer.getDob());
            pstm.setObject(5,customer.getSalary());
            pstm.setObject(6,customer.getAddress());
            pstm.setObject(7,customer.getCity());
            pstm.setObject(8,customer.getProvince());
            pstm.setObject(9,customer.getPostalCode());

            if (pstm.executeUpdate()>0){
                new Alert(Alert.AlertType.INFORMATION,"Customer Added Successfully").show();
                loadTable();
                setNullValueToTextFields();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Customer Added Failed").show();
        }

    }

    @FXML
    void btnViewCustomerOnAction(ActionEvent event) {

    }

    private void loadTable(){
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colBirthday.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));



        try {
            String sql= "Select * from customer";
            ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);
            while (resultSet.next()){
                Customer customer =new Customer(
                        resultSet.getString("CustID"),
                        resultSet.getString("CustTitle"),
                        resultSet.getString("CustName"),
                        resultSet.getDate("dob").toLocalDate(),
                        resultSet.getDouble("salary"),
                        resultSet.getString("CustAddress"),
                        resultSet.getString("city"),
                        resultSet.getString("province"),
                        resultSet.getString("postalCode")
                );

                customerObservableList.add(customer);
            }
            tblCustomer.setItems(customerObservableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String>  customerTitleList= FXCollections.observableArrayList();
        customerTitleList.add("Mr");
        customerTitleList.add("Mrs");
        customerTitleList.add("Miss");
        customerTitleList.add("Ms");

        combTitle.setItems(customerTitleList);
        loadTable();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, customer, newValue) -> {
            if (newValue!=null){
                setValueToTextFields(newValue);
            }
        });
    }
    private void setValueToTextFields(Customer newValue){
        txtID.setText(newValue.getId());
        txtName.setText(newValue.getName());
        txtAddress.setText(newValue.getAddress());
        txtProvince.setText(newValue.getProvince());
        txtCity.setText(newValue.getCity());
        txtSalary.setText(String.valueOf(newValue.getSalary()));
        txtPostalCode.setText(newValue.getPostalCode());
        combTitle.setValue(newValue.getTitle());
        DateBirthDay.setValue(newValue.getDob());
    }
    private void setNullValueToTextFields(){
        System.out.println("Set NUll");
        txtID.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtProvince.setText("");
        txtCity.setText("");
        txtSalary.setText("");
        txtPostalCode.setText("");
        combTitle.setValue("");
    }

    public void btnUpdateCustomerOnAction(ActionEvent actionEvent) {
        String SQL = "UPDATE customer SET CustTitle = ?, CustName = ?, DOB = ?, salary = ?, CustAddress = ?, City = ?, Province = ?, PostalCode = ? WHERE CustID = ?";
        try {
            Customer customer =new Customer(txtID.getText(),combTitle.getValue(),txtName.getText(),DateBirthDay.getValue(),Double.parseDouble(txtSalary.getText()),txtAddress.getText(),txtCity.getText(),txtProvince.getText(),txtPostalCode.getText());
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(SQL);
            pstm.setObject(1,customer.getTitle());
            pstm.setObject(2,customer.getName());
            pstm.setObject(3,customer.getDob());
            pstm.setObject(4,customer.getSalary());
            pstm.setObject(5,customer.getAddress());
            pstm.setObject(6,customer.getCity());
            pstm.setObject(7,customer.getProvince());
            pstm.setObject(8,customer.getPostalCode());
            pstm.setObject(9,customer.getId());
            System.out.println(pstm);
            boolean isUpdated = pstm.executeUpdate() > 0;

            if (isUpdated ){
                new Alert(Alert.AlertType.INFORMATION,"Customer Updated Successful").show();
                loadTable();
                setNullValueToTextFields();
            }else{
                new Alert(Alert.AlertType.ERROR,txtID.getText()+" Customer Not Found").show();
                setNullValueToTextFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,txtID.getText()+" Customer Updated Failed").show();
        }
    }

    public void btnDeleteCustomerOnAction(ActionEvent actionEvent) {
        String SQL = "DELETE FROM customer WHERE CustID = '"+txtID.getText()+"'";
        try {
        boolean isDeleted = DBConnection.getInstance().getConnection().createStatement().executeUpdate(SQL)>0;
        if (isDeleted ){
            new Alert(Alert.AlertType.INFORMATION,"Customer Deleted Successful").show();
            loadTable();
            setNullValueToTextFields();
        }else {
                new Alert(Alert.AlertType.ERROR,"Customer Not Found").show();
                setNullValueToTextFields();
         }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,txtID.getText()+" Customer Delete Error").show();
        }

    }

    public void btnSearchCustomerOnAction(ActionEvent actionEvent) {
        String SQL = "SELECT * FROM customer WHERE CustID=?";
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(SQL);
            pstm.setObject(1,txtID.getText());
            ResultSet resultSet = pstm.executeQuery();
            if (resultSet!=null){
                resultSet.next();
                setValueToTextFields(new Customer(
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
            }else{
                new Alert(Alert.AlertType.ERROR,txtID.getText()+" Customer Not Found").show();
                setNullValueToTextFields();

            }

        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,txtID.getText()+" Customer Search Error").show();
        }
    }
}
