package com.app.shopspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be at least 5 Characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 Characters")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "City name must be at least 4 Characters")
    private String city;

    @NotBlank
    @Size(min = 5, message = "State name must be at least 5 Characters")
    private String state;

    @NotBlank
    @Size(min = 5, message = "Country name must be at least 5 Characters")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pincode must be at least 6 Characters")
    private String pinCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

}
