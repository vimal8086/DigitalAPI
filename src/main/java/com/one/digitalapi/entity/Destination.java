package com.one.digitalapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "destination can not be null..")
    @NotBlank(message = "destination can not be blank..")
    @NotEmpty(message = "destination can not be empty..")
    private String destination;  // Destination name

    public Destination(String destination) {
        this.destination = destination;
    }
}