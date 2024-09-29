package model;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class Customer {
    private String id;
    private String title;
    private String name;
    private LocalDate dob;
    private double salary;
    private String address;
    private String city;
    private String province;
    private String postalCode;

    public Customer(String id, String title, String name, LocalDate dob, double salary, String address, String city, String province, String postalCode) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.dob = dob;
        this.salary = salary;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }
}
