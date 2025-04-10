package com.one.digitalapi.controller;

import com.one.digitalapi.entity.Discount;
import com.one.digitalapi.exception.DiscountException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.DiscountRepository;
import com.one.digitalapi.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.one.digitalapi.exception.GlobalExceptionHandler.getMapResponseEntity;

@RestController
@RequestMapping("/discounts")
@Tag(name = "Discount Management", description = "APIs for managing discounts")
public class DiscountController {


    private static final String CLASSNAME = "DiscountController";

    private static final DefaultLogger LOGGER = new DefaultLogger(DiscountController.class);

    @Autowired
    private DiscountService discountService;

    @Autowired
    private DiscountRepository discountRepository;

    @PostMapping
    @Operation(summary = "Add a new discount", description = "Creates a new discount")
    public ResponseEntity<Discount> addDiscount(@Valid @RequestBody Discount discount) {

        String methodName = "addDiscount";

        if (discountRepository.existsByCode(discount.getCode())) {
            throw new DiscountException("Discount code '" + discount.getCode() + "' already exists.");
        }

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to add discount: {}" + discount);

        Discount createdDiscount = discountService.createDiscount(discount);

        LOGGER.infoLog(CLASSNAME, methodName,"Discount created successfully with ID: {}", createdDiscount.getId());

        return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a discount by ID", description = "Retrieves a discount by its ID")
    public ResponseEntity<Discount> getDiscountById(@PathVariable Long id) {

        String methodName = "getDiscountById";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to retrieve discount with ID: {}", id);

        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountException("Discount not found with id: " + id));

        LOGGER.infoLog(CLASSNAME, methodName, "Discount retrieved successfully: {}" + discount);

        return ResponseEntity.ok(discount);
    }

    @ExceptionHandler(DiscountException.class)
    public ResponseEntity<Map<String, Object>> handleDiscountException(DiscountException ex) {

        String methodName = "handleDiscountException";

        LOGGER.errorLog(CLASSNAME, methodName, "DiscountException occurred: " + ex.getMessage());

        return getMapResponseEntity(ex.getMessage(), ex);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing discount", description = "Updates the details of an existing discount by its ID")
    public ResponseEntity<Discount> updateDiscount(@PathVariable Long id, @Valid @RequestBody Discount discountDetails) {

        String methodName = "updateDiscount";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to update discount with ID: {}", id);

        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountException("Discount not found with id: " + id));

        // Check for duplicate discount code
        if (!existingDiscount.getCode().equals(discountDetails.getCode()) &&
                discountRepository.existsByCode(discountDetails.getCode())) {
            throw new DiscountException("Discount code '" + discountDetails.getCode() + "' already exists.");
        }

        existingDiscount.setCode(discountDetails.getCode());
        existingDiscount.setDescription(discountDetails.getDescription());
        existingDiscount.setStartDate(discountDetails.getStartDate());
        existingDiscount.setEndDate(discountDetails.getEndDate());
        existingDiscount.setType(discountDetails.getType());
        existingDiscount.setUsageLimit(discountDetails.getUsageLimit());
        existingDiscount.setValue(discountDetails.getValue());

        Discount updatedDiscount = discountService.updateDiscount(existingDiscount);

        LOGGER.infoLog(CLASSNAME, methodName, "Discount updated successfully: {}" + updatedDiscount);

        return ResponseEntity.ok(updatedDiscount);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a discount", description = "Deletes a discount by its ID")
    public ResponseEntity<Map<String, String>> deleteDiscount(@PathVariable Long id) {

        String methodName = "deleteDiscount";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to delete discount with ID: {}", id);

        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountException("Discount not found with id: " + id));

        discountService.deleteDiscount(existingDiscount);

        LOGGER.infoLog(CLASSNAME, methodName, "Discount deleted successfully with ID: {}", id);

        Map<String, String> response = new HashMap<>();

        response.put("message", "Discount deleted successfully.");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all discounts", description = "Retrieves a list of all discounts")
    public ResponseEntity<List<Discount>> getAllDiscounts() {

        String methodName = "getAllDiscounts";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to getAllDiscounts: {}");

        List<Discount> discounts = discountService.getAllDiscounts();

        LOGGER.infoLog(CLASSNAME, methodName, "Discount Retrieves successfully: {}");

        return ResponseEntity.ok(discounts);
    }
}