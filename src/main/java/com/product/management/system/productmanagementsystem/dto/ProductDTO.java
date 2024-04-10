package com.product.management.system.productmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    private String sku;

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal weight;

    private String weightUnit;

    private String brand;

    private String category;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp expiryDate;

    private Integer inventory;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    public ProductDTO() {
    }

    public ProductDTO(String sku, String name, String description, BigDecimal price, BigDecimal weight, String weightUnit, String brand, String category, Timestamp expiryDate, Integer inventory, Long userId) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.brand = brand;
        this.category = category;
        this.expiryDate = expiryDate;
        this.inventory = inventory;
        this.userId = userId;
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

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                ", weightUnit='" + weightUnit + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", expiryDate=" + expiryDate +
                ", inventory=" + inventory +
                ", userId=" + userId +
                '}';
    }
}
