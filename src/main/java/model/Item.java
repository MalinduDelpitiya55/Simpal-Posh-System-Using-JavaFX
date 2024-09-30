package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Item {
    private String itemCode;
    private String description;
    private String packageSize;
    private double unitPrice;
    private int qtyOnHand;

}
