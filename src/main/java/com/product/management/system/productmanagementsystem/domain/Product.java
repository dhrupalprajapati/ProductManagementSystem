package com.product.management.system.productmanagementsystem.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull(message = "Sku cannot be null")
    private String sku;

    @NotNull(message = "Name cannot be null")
    @Size(max = 20, message = "Name must be in between 0-20 characters")
    private String name;

    @NotNull(message = "Description cannot be null")
    @Size(min = 10, max = 200, message = "Description must be between 10-200 characters")
    private String description;

    @Column(precision = 19, scale = 2)
    private BigDecimal price;

    @Column(precision = 19, scale = 2)
    private BigDecimal weight;

    @NotNull(message = "Unit of weight cannot be null")
    private String weightUnit;

    @NotNull(message = "Brand name cannot be null")
    private String brand;

    @NotNull(message = "Category of the product cannot be null")
    private String category;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp expiryDate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Min(value = 0, message = "Inventory should not be less than 0")
    @Max(value = 150000, message = "Inventory should not be more than 150000")
    private int inventory;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp created;

    public Product() {
    }

    public Product(Long id, String sku, String name, String description, BigDecimal price, BigDecimal weight, String weightUnit, String brand, String category, Timestamp expiryDate, User user, int inventory, Timestamp created) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.brand = brand;
        this.category = category;
        this.expiryDate = expiryDate;
        this.user = user;
        this.inventory = inventory;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
