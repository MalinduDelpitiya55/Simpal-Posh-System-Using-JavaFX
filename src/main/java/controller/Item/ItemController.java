package controller.Item;

import controller.customer.CustomerServices;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.Item;
import util.CrudUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemController implements ItemServices {
    @Override
    public boolean addItem(Item item) {
        String SQL = "INSERT INTO item VALUES(?,?,?,?,?)";
        try {
            return CrudUtils.execute(
                    SQL,
                    item.getItemCode(),
                    item.getDescription(),
                    item.getPackageSize(),
                    item.getUnitPrice(),
                    item.getQtyOnHand()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Item searchItem(String itemCode) {
        try {
            ResultSet resultSet = CrudUtils.execute(
                    "SELECT * FROM item WHERE ItemCode= ?",
                    itemCode
            );
            resultSet.next();
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getInt(5)
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateItem(Item item) {
        String SQL = "UPDATE item SET  Description = ?, PackSize = ?, UnitPrice = ?, QtyOnHand = ? WHERE ItemCode = ?";
        try {
            return CrudUtils.execute(
                    SQL,
                    item.getDescription(),
                    item.getPackageSize(),
                    item.getUnitPrice(),
                    item.getQtyOnHand(),
                    item.getItemCode()
            );
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteItem(String itemCode) {
        String SQL = "DELETE FROM item WHERE ItemCode = ?";
        try {
            return CrudUtils.execute(SQL,itemCode);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Item> getAllItem() {
        String sql= "Select * from item";
        ObservableList<Item> ItemObservableList = FXCollections.observableArrayList();
        try {

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
            return ItemObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ObservableList<String> getItemCodes(){
        ObservableList<Item> allItems = getAllItem();
        ObservableList<String> codeList = FXCollections.observableArrayList();

        allItems.forEach(items->{
            codeList.add(items.getItemCode());
        });

        return codeList;
    }
}
