package com.product.management.system.productmanagementsystem.repository;

import com.product.management.system.productmanagementsystem.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Transactional
    public void deleteBySku(String sku);

    public Optional<Product> findBySku(String sku);

}
