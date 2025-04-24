package com.coffeeshop.backend.repository;

import com.coffeeshop.backend.entity.CafeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeTableRepository extends JpaRepository<CafeTable, Long> {
    boolean existsByTableNumber(int tableNumber);
}
