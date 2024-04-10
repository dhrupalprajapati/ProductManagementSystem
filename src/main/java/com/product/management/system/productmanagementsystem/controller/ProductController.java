package com.product.management.system.productmanagementsystem.controller;

import com.product.management.system.productmanagementsystem.dto.ProductDTO;
import com.product.management.system.productmanagementsystem.service.ProductService;
import com.product.management.system.productmanagementsystem.util.UserNotAuthenticatedException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Set;

import static com.product.management.system.productmanagementsystem.util.Utility.getUserIdFromSession;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getProducts(HttpSession session) {
        Long userId = null;
        try {
            userId = getUserIdFromSession(session);
            Set<ProductDTO> products = productService.getProducts(userId);
            LOGGER.info("getProducts :: product fetched successfully for userId: {}", userId);
            return ResponseEntity.ok(products);
        } catch (UserNotAuthenticatedException e) {
            LOGGER.error("getProducts :: got UserNotAuthenticatedException", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            LOGGER.error("getProducts :: got NoSuchElementException for requested userId: {}", userId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOGGER.error("getProducts :: got Exception for requested userId: {}", userId, e);
            return ResponseEntity.internalServerError().body("Something went worng on server side!");
        }
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO, HttpSession session) {
        Long userId = null;
        try {
            userId = getUserIdFromSession(session);
            productService.saveProduct(productDTO, userId);
            LOGGER.info("createProduct :: product created successfully with requested productDTO: {} for userId: {}", productDTO, userId);
            return ResponseEntity.ok("Product created successfully!");
        } catch (UserNotAuthenticatedException e) {
            LOGGER.error("createProduct :: got UserNotAuthenticatedException", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("createProduct :: got DataIntegrityViolationException for requested productDTO: {}, userId: {}", productDTO, userId, e);
            return ResponseEntity.badRequest().body("Sku already exists in database, please provide unique sku.");
        } catch (NoSuchElementException e) {
            LOGGER.error("createProduct :: got NoSuchElementException for requested productDTO: {}, userId: {}", productDTO, userId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> updateProduct(@RequestBody ProductDTO productDTO, HttpSession session) {
        Long userId = null;
        try {
            userId = getUserIdFromSession(session);
            productService.updateProduct(productDTO, userId);
            LOGGER.info("updateProduct :: product updated successfully with requested productDTO: {} for userId: {}", productDTO, userId);
            return ResponseEntity.ok("Product updated successfully!");
        } catch (UserNotAuthenticatedException e) {
            LOGGER.error("updateProduct :: got UserNotAuthenticatedException", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }  catch (DataIntegrityViolationException e) {
            LOGGER.error("updateProduct :: got DataIntegrityViolationException for requested productDTO: {}, userId: {}", productDTO, userId, e);
            return ResponseEntity.badRequest().body("Sku already exists in database, please provide unique sku.");
        } catch (NoSuchElementException e) {
            LOGGER.error("updateProduct :: got NoSuchElementException for requested productDTO: {}, userId: {}", productDTO, userId, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<?> deleteProduct(@PathVariable(name = "sku") String sku, HttpSession session) {
        Long userId = null;
        try {
            userId = getUserIdFromSession(session);
            productService.deleteProduct(sku);
            LOGGER.info("deleteProduct :: product deleted successfully with requested sku: {} for userId: {}", sku, userId);
            return ResponseEntity.ok("Product deleted successfully!");
        }  catch (UserNotAuthenticatedException e) {
            LOGGER.error("deleteProduct :: got UserNotAuthenticatedException", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("deleteProduct :: got error while deleting product with requested sku: {} for userId: {}", sku, userId, e);
            return ResponseEntity.internalServerError().body("Something went wrong while delete the product!");
        }
    }
}
