package com.server.app.repository;

import com.server.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByProductId(Long id);

    void deleteProductByProductId(Long id);

    boolean existsProductByProductId(Long id);

    Product getProductByProductId(Long id);
}