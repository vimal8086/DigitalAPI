package com.one.digitalapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "destinations")  // This maps the entity to the "destinations" table
@Getter
@Setter
@NoArgsConstructor
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Primary Key (Auto-increment)

    @Column(nullable = false, unique = true)
    private String destination;  // Destination name

    public Destination(String destination) {
        this.destination = destination;
    }
}