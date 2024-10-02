package controller.Item;

import javafx.collections.ObservableList;
import model.Item;

public interface ItemServices {
    boolean addItem(Item item);
    Item searchItem(String id);
    boolean updateItem(Item item);
    boolean deleteItem(String id);
    ObservableList<Item> getAllItem();

}
