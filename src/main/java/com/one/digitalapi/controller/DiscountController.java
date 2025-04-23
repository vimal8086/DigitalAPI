package com.one.digitalapi.controller;

import com.one.digitalapi.entity.Discount;
import com.one.digitalapi.entity.DiscountImage;
import com.one.digitalapi.exception.DiscountException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.DiscountRepository;
import com.one.digitalapi.service.DiscountImageService;
import com.one.digitalapi.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

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

    @Autowired
    private DiscountImageService discountImageService;


    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Add a new discount",
            description = "Creates a new discount",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "413", description = "Image size exceeds 10MB")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Discount> addDiscount(
            @Valid @ModelAttribute Discount discountRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {

        Discount discount = new Discount();
        BeanUtils.copyProperties(discountRequest, discount);

        if (imageFile != null && !imageFile.isEmpty()) {
            DiscountImage savedImage = discountImageService.saveImage(imageFile);
            discount.setImage(savedImage);  // Set relation
        }

        Discount createdDiscount = discountService.createDiscount(discount);
        return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a discount by ID", description = "Retrieves a discount by its ID")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Discount> getDiscountById(@PathVariable Long id) {

        String methodName = "getDiscountById";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to retrieve discount with ID: {}", id);

        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountException("Discount not found with id: " + id));

        LOGGER.infoLog(CLASSNAME, methodName, "Discount retrieved successfully: {}" + discount);

        return ResponseEntity.ok(discount);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing discount", description = "Updates the details of an existing discount by its ID")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discount>> getAllDiscounts() {

        String methodName = "getAllDiscounts";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to getAllDiscounts: {}");

        List<Discount> discounts = discountService.getAllDiscounts();

        List<Discount> discountList = new ArrayList<>();
        if (discounts != null && !discounts.isEmpty()) {
            for (Discount discount : discounts) {
                if (discount.getImage() != null && discount.getImage().getId() != null) {
                    try {

                        // For Particular image retrieval (Admin Can See Image is proper or not)
                        discount.setDiscountImageId(discount.getImage().getId());

                        byte[] imageBytes = discountImageService.getImage(discount.getImage().getId());

                        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                        String mimeType = MediaType.IMAGE_PNG_VALUE; // adjust based on actual image type if needed
                        String imageWithMime = "data:" + mimeType + ";base64," + base64Image;

                        discount.setDiscountImage(imageWithMime);
                    } catch (RuntimeException ex) {
                        discount.setDiscountImage(null); // fallback
                    }
                }
                discountList.add(discount);
            }
        }

        LOGGER.infoLog(CLASSNAME, methodName, "Discount Retrieves successfully: {}");

        return ResponseEntity.ok(discountList);
    }

    @GetMapping("/image/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        byte[] image = discountImageService.getImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }

    @ExceptionHandler(DiscountException.class)
    public ResponseEntity<Map<String, Object>> handleDiscountException(DiscountException ex) {
        String methodName = "handleDiscountException";
        LOGGER.errorLog(CLASSNAME, methodName, "DiscountException occurred: " + ex.getMessage());
        return getMapResponseEntity(ex.getMessage(), ex);
    }
}