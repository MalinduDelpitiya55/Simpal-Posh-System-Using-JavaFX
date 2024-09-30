
package controller;

        import com.jfoenix.controls.JFXButton;
        import com.jfoenix.controls.JFXTextField;
        import db.DBConnection;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.Alert;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.cell.PropertyValueFactory;
        import model.Customer;
        import model.Item;
        import util.CrudUtils;

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

    @FXML
    void ReloadOnAction(ActionEvent event) {
        loadTable();

    }

    private void loadTable(){
        ObservableList<Item> ItemObservableList = FXCollections.observableArrayList();
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPack.setCellValueFactory(new PropertyValueFactory<>("packageSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        try {
            String sql= "Select * from item";
            ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);
            while (resultSet.next()){
                Item item =new Item(
                        resultSet.getString("ItemCode"),
                        resultSet.getString("Description"),
                        resultSet.getString("PackSize"),
                        resultSet.getDouble("UnitPrice"),
                        resultSet.getInt("QtyOnHand")
                );

                ItemObservableList.add(item);
            }
            tblItem.setItems(ItemObservableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        String SQL = "INSERT INTO item VALUES(?,?,?,?,?)";
        try {
            boolean isItemAdded = CrudUtils.execute(SQL,item.getItemCode(),item.getDescription(),item.getPackageSize(),item.getUnitPrice(),item.getQtyOnHand());

            if (isItemAdded){
                new Alert(Alert.AlertType.INFORMATION,"Item Added Successfully").show();
                loadTable();
                setNullValueToTextFields();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Item Added Failed").show();
        }
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteItemOnAction(ActionEvent event) {
        String SQL = "DELETE FROM item WHERE ItemCode = ?";
        try {
            boolean isDeleted = CrudUtils.execute(SQL,txtItemCode.getText());
            if (isDeleted ){
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted Successful").show();
                loadTable();
                setNullValueToTextFields();
            }else {
                new Alert(Alert.AlertType.ERROR,"Item Not Found").show();
                setNullValueToTextFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,txtItemCode.getText()+" Item Delete Error").show();
        }
    }

    @FXML
    void btnSearchItemOnAction(ActionEvent event) {
        String SQL = "SELECT * FROM item WHERE ItemCode=?";
        try {

            ResultSet resultSet = CrudUtils.execute(SQL,txtItemCode.getText());
            if (resultSet!=null){
                resultSet.next();
                setValueToTextFields(new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                ));
            }else{
                new Alert(Alert.AlertType.ERROR,txtItemCode.getText()+" Item Not Found").show();
                setNullValueToTextFields();

            }

        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,txtItemCode.getText()+" Item Search Error").show();
        }
    }

    @FXML
    void btnUpdateItemOnAction(ActionEvent event) {
        String SQL = "UPDATE item SET  Description = ?, PackSize = ?, UnitPrice = ?, QtyOnHand = ? WHERE ItemCode = ?";
        try {
            Item item =new Item(txtItemCode.getText(),txtDescription.getText(),txtPackSize.getText(),Double.parseDouble(txtUnitPrice.getText()),Integer.parseInt(txtQtyOnHand.getText()));

            boolean isUpdated = CrudUtils.execute(SQL,item.getDescription(),item.getPackageSize(),item.getUnitPrice(),item.getQtyOnHand(),item.getItemCode());;

            if (isUpdated ){
                new Alert(Alert.AlertType.INFORMATION,"Item Updated Successful").show();
                loadTable();
                setNullValueToTextFields();
            }else{
                new Alert(Alert.AlertType.ERROR,txtItemCode.getText()+" Item Not Found").show();
                setNullValueToTextFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,txtItemCode.getText()+" Item Updated Failed").show();
        }
    }

}

