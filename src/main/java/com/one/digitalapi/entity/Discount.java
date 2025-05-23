package com.one.digitalapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Discount code cannot be null")
    @NotBlank(message = "Discount code cannot be blank")
    @Column(unique = true)
    private String code;

    private String description;

    @NotNull(message = "Discount type cannot be null")
    @Enumerated(EnumType.STRING)
    private DiscountType type; // PERCENTAGE or FLAT

    @NotNull(message = "Discount value cannot be null")
    @Positive(message = "Discount value must be positive")
    private Integer value;

    @NotNull(message = "startDate cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    private Integer usageLimit;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    @JsonIgnore
    private DiscountImage image;

    @Transient
    private String discountImage;

    @Transient
    private Long discountImageId;

    public Long getDiscountImageId() {
        return discountImageId;
    }

    public void setDiscountImageId(Long discountImageId) {
        this.discountImageId = discountImageId;
    }

    public String getDiscountImage() {
        return discountImage;
    }

    public void setDiscountImage(String discountImage) {
        this.discountImage = discountImage;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public DiscountImage getImage() {
        return image;
    }

    public void setImage(DiscountImage image) {
        this.image = image;
    }
}