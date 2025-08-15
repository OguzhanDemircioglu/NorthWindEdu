package com.server.app.repository;

import com.server.app.dto.response.ProductDto;
import com.server.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Short> {

    Optional<Product> findProductByProductId(Short id);

    void deleteProductByProductId(Short id);
}
