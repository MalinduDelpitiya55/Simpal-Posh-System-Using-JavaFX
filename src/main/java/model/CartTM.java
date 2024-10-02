package model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartTM {
    private String itemCode;
    private String description;
    private Double unitPrice;
    private int qty;
    private double total;
}
