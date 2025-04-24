package com.coffeeshop.backend.service;

import com.coffeeshop.backend.entity.CafeTable;
import com.coffeeshop.backend.repository.CafeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CafeTableService {

    @Autowired
    private CafeTableRepository repo;

    public CafeTable createTable(CafeTable table) {
        if (repo.existsByTableNumber(table.getTableNumber())) {
            throw new RuntimeException("Số bàn đã tồn tại");
        }
        return repo.save(table);
    }

    public List<CafeTable> getAllTables() {
        return repo.findAll();
    }

    public CafeTable updateTable(Long id, CafeTable tableDetails) {
        CafeTable table = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));

        table.setTableNumber(tableDetails.getTableNumber());
        table.setDescription(tableDetails.getDescription());
        table.setStatus(tableDetails.getStatus());

        return repo.save(table);
    }

    public void deleteTable(Long id) {
        repo.deleteById(id);
    }
}
