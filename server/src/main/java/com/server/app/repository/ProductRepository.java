package com.server.app.repository;

import com.server.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByProductId(Long id);

    void deleteProductByProductId(Long id);

    boolean existsProductByProductId(Long id);

    Product getProductByProductId(Long id);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE product_seq RESTART WITH 1", nativeQuery = true)
    void resetProductSequence();
}