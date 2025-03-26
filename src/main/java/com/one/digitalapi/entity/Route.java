package com.one.digitalapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routeID;

    @Column(nullable = false)
    @NotNull(message = "route from can not be null..")
    @NotBlank(message = "route from can not be blank..")
    @NotEmpty(message = "route from can not be empty..")
    private String routeFrom;

    @Column(nullable = false)
    @NotNull(message = "route to can not be null..")
    @NotBlank(message = "route to can not be blank..")
    @NotEmpty(message = "route to can not be empty..")
    private String routeTo;

    @Column(nullable = false)
    @NotNull(message = "Distance cannot be null")
    @Min(value = 1, message = "Distance must be at least 1 km")
    private Double distance;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "route")
    private List<Bus> bus;

}
