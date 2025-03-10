package com.one.digitalapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    private String routeFrom;

    @Column(nullable = false)
    private String routeTo;

    @Column(nullable = false)
    private double distance;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "route")
    private List<Bus> bus;

}
