package com.product.management.system.productmanagementsystem.service;

import com.product.management.system.productmanagementsystem.domain.Product;
import com.product.management.system.productmanagementsystem.domain.User;
import com.product.management.system.productmanagementsystem.dto.ProductDTO;
import com.product.management.system.productmanagementsystem.repository.ProductRepository;
import com.product.management.system.productmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static com.product.management.system.productmanagementsystem.util.Utility.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserService userService1;

    User user;

    Product product1;

    Product product2;

    Set<Product> products = new HashSet<>();

    @BeforeEach
    public void init() {
        user = new User(1L, "dhrupal", "dhrupal@gmail.com", "test_pass", Set.of());
        product1 = new Product(1L, "GARM-01110-N", "Garmin watch", "Garmin smart watch", new BigDecimal(129.99), new BigDecimal(12.00), "POUND", "Garmin", "Wearables", Timestamp.valueOf("2030-04-30 00:00:00"), user, 120, Timestamp.valueOf("2024-04-30 00:00:00"));
        product2 = new Product(1L, "GARM-02220-N", "Garmin watch edition 2", "Garmin smart watch edition 2", new BigDecimal(149.99), new BigDecimal(12.00), "POUND", "Garmin", "Wearables", Timestamp.valueOf("2030-04-30 00:00:00"), user, 10, Timestamp.valueOf("2024-04-30 00:00:00"));
        products = Set.of(product1, product2);
    }

    @Test
    public void getProductsWithEmptySetTest() {
        Long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        var products = userService1.findUserById(userId).get().getProducts();
        assertThat(products).isEmpty();
    }

    @Test
    public void getProductsWithProductsFoundTest() throws CloneNotSupportedException {
        Long userId = 1L;

        User clonnedUser = user.clone();
        clonnedUser.setProducts(products);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(clonnedUser));

        var products = userService1.findUserById(userId).get().getProducts();
        assertThat(products).isNotEmpty();
    }

    @Test
    public void saveProductWhenUserNotFoundTest() {
        ProductDTO productDTO = objectMapper.convertValue(product1, ProductDTO.class);
        Long userId = 1L;
        when(userService.findUserById(userId)).thenReturn(Optional.empty());
        assertThrows(
                NoSuchElementException.class,
                () -> productService.saveProduct(productDTO, userId)
        );
    }

    @Test
    public void saveProductWhenUserExistsTest() {
        ProductDTO productDTO = objectMapper.convertValue(product1, ProductDTO.class);
        Long userId = 1L;
        productDTO.setUserId(userId);
        when(userService.findUserById(userId)).thenReturn(Optional.of(user));
        productService.saveProduct(productDTO, userId);
    }

    @Test
    public void updateProductWithUserNotFoundTest() {
        Long userId = 2L;

        ProductDTO productDTO = new ProductDTO();
        productDTO.setUserId(userId);

        when(userService.findUserById(userId)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> productService.updateProduct(productDTO, userId)
        );

        String expectedMessage = "User not found with user id: " + userId;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, expectedMessage);
    }

    @Test
    public void updateProductWithProductNotFoundTest() {
        Long userId = 1L;
        String sku = "XYZ456";

        ProductDTO productDTO = new ProductDTO();
        productDTO.setUserId(userId);
        productDTO.setSku(sku);

        User user = new User();
        user.setId(userId);
        user.setProducts(Set.of());

        when(userService.findUserById(userId)).thenReturn(Optional.of(user));

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> productService.updateProduct(productDTO, userId)
        );

        String expectedMessage = "Product not found with given sku: " + sku;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, expectedMessage);
    }

    @Test
    public void updateProductWithUserAndProductExistTest() throws CloneNotSupportedException {
        Long userId = 1L;
        String sku = "GARM-01110-N";

        ProductDTO productDTO = new ProductDTO();
        productDTO.setUserId(userId);
        productDTO.setSku(sku);

        User user = new User();
        user.setId(userId);
        User clonnedUser = user.clone();

        clonnedUser.setProducts(products);

        Product existingProduct = new Product();
        existingProduct.setId(userId);
        existingProduct.setSku(sku);
        existingProduct.setUser(user);

        when(userService.findUserById(userId)).thenReturn(Optional.of(clonnedUser));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product updatedProduct = productService.updateProduct(productDTO, userId);
        assertEquals(existingProduct, updatedProduct, "Performed product updation.");
    }

    @Test
    public void deleteProductWithProductExistsTest() {
        String sku = "GARM-01110-N";
        when(productRepository.findBySku(sku)).thenReturn(Optional.of(product1));
        assertAll(() -> productService.deleteProduct(sku));
    }

    @Test
    public void deleteProductWithProductNotFoundTest() {
        String sku = "APPLE-0110-N";

        when(productRepository.findBySku(sku)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.deleteProduct(sku)
        );

        String expectedMessage = "Please provide valid sku for product deletion!";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage, "Performed product deletion.");
    }

}
