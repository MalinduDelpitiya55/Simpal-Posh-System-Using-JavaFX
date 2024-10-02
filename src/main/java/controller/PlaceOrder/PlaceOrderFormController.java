package controller.PlaceOrder;

import com.jfoenix.controls.JFXTextField;
import controller.Item.ItemController;
import controller.customer.CustomerController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.CartTM;
import model.Customer;
import model.Item;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class PlaceOrderFormController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        combItemCode.getSelectionModel().selectedItemProperty().addListener(((observableValue, s, newValue) -> {
            loadItemData(newValue);
        }));
        combCustomerID.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newValue) -> {
            System.out.println(newValue);
            loadCustomerData(newValue);
        });

        loadDateAndTime();
        loadCustomerID();
        loadItemCodes();
    }
    @FXML
    private ComboBox<?> CombItemCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private ComboBox<String> combCustomerID;

    @FXML
    private ComboBox<String> combItemCode;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblNetTotal;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<CartTM> tblCart;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtOrderID;

    @FXML
    private JFXTextField txtQTY;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    void btnAddOrderOnAction(ActionEvent event) {

    }

    ObservableList<CartTM> cart = FXCollections.observableArrayList();
    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Integer qty = Integer.parseInt(txtQTY.getText());

        Double total = unitPrice * qty;
        cart.add(
                new CartTM(
                        combItemCode.getValue(),
                        txtDescription.getText(),
                        unitPrice,
                        qty,
                        total
                )
        );
        tblCart.setItems(cart);
        calcNetTotal();
    }
    private void calcNetTotal() {
        Double total = 0.0;

        for (CartTM cartTM : cart) {
            total += cartTM.getTotal();
        }

        lblNetTotal.setText(total.toString());
    }

    private void loadDateAndTime(){
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        Timeline timeline =new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime now = LocalTime.now();
            lblTime.setText(String.format(" %02d : %02d : %02d", now.getHour(), now.getMinute(), now.getSecond()));
        }),
                new KeyFrame(Duration.seconds(1))

        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadCustomerID(){
        combCustomerID.setItems(new CustomerController().getCustomerIds());
    }
    private void loadItemCodes(){

        combItemCode.setItems(new ItemController().getItemCodes());
    }
    private void loadItemData(String itemCode){
        Item item = new ItemController().searchItem(itemCode);

        txtDescription.setText(item.getDescription());
        txtStock.setText(String.valueOf(item.getQtyOnHand()));
        txtUnitPrice.setText(String.valueOf( item.getUnitPrice()));

    }
    private void loadCustomerData(String custID){
        Customer customer = new CustomerController().searchCustomer(custID);

        txtName.setText(customer.getName());
        txtCity.setText(String.valueOf(customer.getCity()));
        txtSalary.setText(String.valueOf( customer.getSalary()));

    }

}
