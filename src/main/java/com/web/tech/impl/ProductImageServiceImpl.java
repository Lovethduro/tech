
package com.web.tech.impl;

import com.web.tech.model.ProductImages;
import com.web.tech.repository.ProductImageRepository;
import com.web.tech.service.ProductImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class ProductImageServiceImpl extends ProductImageService {
    private static final Logger logger = LoggerFactory.getLogger(ProductImageServiceImpl.class);

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public ProductImages saveImage(String productId, MultipartFile file) throws IOException {
        logger.debug("Saving image for productId: {}", productId);
        if (file.isEmpty()) {
            logger.error("Empty file provided for productId: {}", productId);
            throw new IOException("File is empty");
        }

        ProductImages image = new ProductImages();
        image.setProductId(productId);
        image.setContentType(file.getContentType());
        image.setImageData(file.getBytes());
        image.setFileSize(file.getSize());
        image.setUploadDate(LocalDateTime.now());

        ProductImages savedImage = productImageRepository.save(image);
        logger.info("Image saved successfully for productId: {}", productId);
        return savedImage;
    }

    @Override
    public Optional<ProductImages> getImageByProductId(String productId) {
        logger.debug("Fetching image for productId: {}", productId);
        return productImageRepository.findByProductId(productId);
    }

    @Override
    public String getProductImageBase64(String productId) {
        logger.debug("Fetching Base64 image for productId: {}", productId);
        Optional<ProductImages> imageOpt = getImageByProductId(productId);
        if (imageOpt.isPresent()) {
            byte[] imageData = imageOpt.get().getImageData();
            return Base64.getEncoder().encodeToString(imageData);
        }
        logger.warn("No image found for productId: {}", productId);
        return null;
    }

    @Override
    public String getProductImageContentType(String productId) {
        logger.debug("Fetching content type for productId: {}", productId);
        Optional<ProductImages> imageOpt = getImageByProductId(productId);
        return imageOpt.map(ProductImages::getContentType).orElse(null);
    }

    @Override
    public void deleteImage(String productId) throws IOException {
        logger.debug("Deleting image for productId: {}", productId);
        Optional<ProductImages> imageOpt = getImageByProductId(productId);
        if (imageOpt.isPresent()) {
            productImageRepository.deleteById(imageOpt.get().getId());
            logger.info("Image deleted successfully for productId: {}", productId);
        } else {
            logger.warn("No image found to delete for productId: {}", productId);
            throw new IOException("Image not found for productId: " + productId);
        }
    }
}
