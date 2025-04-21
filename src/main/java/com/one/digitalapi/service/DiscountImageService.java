package com.one.digitalapi.service;

import com.one.digitalapi.entity.DiscountImage;
import com.one.digitalapi.repository.DiscountImageRepository;
import com.one.digitalapi.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class DiscountImageService {

    @Autowired
    private DiscountImageRepository discountImageRepository;

    public DiscountImage saveImage(MultipartFile file) {
        DiscountImage image = null;
        try {
            image = DiscountImage.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtil.compressImage(file.getBytes()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return discountImageRepository.save(image);
    }

    public byte[] getImage(Long imageId) {
        DiscountImage image = discountImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        return ImageUtil.decompressImage(image.getImageData());
    }
}