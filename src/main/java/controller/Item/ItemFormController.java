
package controller.Item;

        import com.jfoenix.controls.JFXButton;
        import com.jfoenix.controls.JFXTextField;
        import db.DBConnection;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.fxml.Initializable;
        import javafx.scene.Scene;
        import javafx.scene.control.Alert;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.stage.Stage;
        import model.Customer;
        import model.Item;
        import util.CrudUtils;

        import java.io.IOException;
        import java.net.URL;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.ResourceBundle;

public class ItemFormController implements Initializable {

    @FXML
    private JFXButton btnReload;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colPack;

    @FXML
    private TableColumn<?, ?> colQtyOnHand;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<Item> tblItem;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextField txtPackSize;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtUnitPrice;

    ItemController itemController = new ItemController();
    @FXML
    void ReloadOnAction(ActionEvent event) {
        loadTable();

    }

    private void loadTable(){
        ObservableList<Item> ItemObservableList = itemController.getAllItem();
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPack.setCellValueFactory(new PropertyValueFactory<>("packageSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        tblItem.setItems(ItemObservableList);

    }
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadTable();

        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, Item, newValue) -> {
            if (newValue!=null){
                setValueToTextFields(newValue);
            }
        });
    }
    private void setValueToTextFields(Item newValue){
        txtItemCode.setText(newValue.getItemCode());
        txtDescription.setText(newValue.getDescription());
        txtPackSize.setText(newValue.getPackageSize());
        txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(newValue.getQtyOnHand()));

    }
    private void setNullValueToTextFields(){
        txtItemCode.setText("");
        txtDescription.setText("");
        txtPackSize.setText("");
        txtUnitPrice.setText("");
        txtQtyOnHand.setText("");
    }

    @FXML
    void btnAddItemOnAction(ActionEvent event) {
        Item item =new Item(txtItemCode.getText(),txtDescription.getText(),txtPackSize.getText(),Double.parseDouble(txtUnitPrice.getText()),Integer.parseInt(txtQtyOnHand.getText()));

            boolean isItemAdded = itemController.addItem(item);
            if (isItemAdded){
                new Alert(Alert.AlertType.INFORMATION,"Item Added Successfully").show();
                loadTable();
                setNullValueToTextFields();
            }

    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Dashbord.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) {

            if (itemController.deleteItem(txtItemCode.getText()) ){
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted Successful").show();
                loadTable();
                setNullValueToTextFields();
            }else {
                new Alert(Alert.AlertType.ERROR,"Item Not Found").show();
                setNullValueToTextFields();
            }

    }

    @FXML
    void btnSearchItemOnAction(ActionEvent event) {
        setValueToTextFields(itemController.searchItem(txtItemCode.getText()));
    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) {
            Item item =new Item(txtItemCode.getText(),txtDescription.getText(),txtPackSize.getText(),Double.parseDouble(txtUnitPrice.getText()),Integer.parseInt(txtQtyOnHand.getText()));
            if (itemController.updateItem(item)){
                new Alert(Alert.AlertType.INFORMATION,"Item Updated Successful").show();
                loadTable();
                setNullValueToTextFields();
            }else{
                new Alert(Alert.AlertType.ERROR,txtItemCode.getText()+" Item Not Found").show();
                setNullValueToTextFields();
            }
    }

}

