package com.jk.mcp.repository;

import com.jk.mcp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByProduct(String lowerCaseName);

    Cart findByProductIgnoreCase(String productName);
}
