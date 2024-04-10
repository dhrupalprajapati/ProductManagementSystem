package com.product.management.system.productmanagementsystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.product.management.system.productmanagementsystem.domain.Product;
import com.product.management.system.productmanagementsystem.domain.User;
import com.product.management.system.productmanagementsystem.dto.ProductDTO;
import com.product.management.system.productmanagementsystem.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static com.product.management.system.productmanagementsystem.util.Utility.objectMapper;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    public Set<ProductDTO> getProducts(Long userId) {
        Optional<User> userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found!");
        }
        if (userOptional.get().getProducts().isEmpty()) {
            throw new NoSuchElementException();
        }
        Set<ProductDTO> productDTOS = objectMapper.convertValue(userOptional.get().getProducts(), new TypeReference<Set<ProductDTO>>() {
        });
        return productDTOS;
    }

    public Product saveProduct(ProductDTO productDTO, Long userId) {
        Optional<User> userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User not found with user id: " + productDTO.getUserId());
        }
        Product product = objectMapper.convertValue(productDTO, Product.class);
        product.setUser(userOptional.get());
        return productRepository.save(product);
    }

    public Product updateProduct(ProductDTO productDTO, Long userId) {
        Optional<User> userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User not found with user id: " + productDTO.getUserId());
        }
        User user = userOptional.get();
        Optional<Product> savedProductOptional = user.getProducts().stream().filter(p -> p.getSku().equalsIgnoreCase(productDTO.getSku())).findFirst();
        if (savedProductOptional.isEmpty()) {
            throw new NoSuchElementException("Product not found with given sku: " + productDTO.getSku());
        }
        Product newProduct = objectMapper.convertValue(productDTO, Product.class);
        newProduct.setId(savedProductOptional.get().getId());
        newProduct.setUser(user);
        return productRepository.save(newProduct);
    }

    public void deleteProduct(String sku) {
        if (productRepository.findBySku(sku).isEmpty()) {
            throw new RuntimeException("Please provide valid sku for product deletion!");
        }
        productRepository.deleteBySku(sku);
    }
}
