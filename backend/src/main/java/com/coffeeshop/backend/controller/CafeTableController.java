package com.coffeeshop.backend.controller;

import com.coffeeshop.backend.entity.CafeTable;
import com.coffeeshop.backend.service.CafeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
public class CafeTableController {

    @Autowired
    private CafeTableService service;

    // ✅ Chỉ ADMIN được tạo bàn
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CafeTable> create(@RequestBody CafeTable table) {
        return ResponseEntity.ok(service.createTable(table));
    }

    // ✅ Cả ADMIN và USER đều được xem
    @GetMapping
    public List<CafeTable> getAll() {
        return service.getAllTables();
    }

    // ✅ Chỉ ADMIN được cập nhật
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CafeTable> update(@PathVariable Long id, @RequestBody CafeTable table) {
        return ResponseEntity.ok(service.updateTable(id, table));
    }

    // ✅ Chỉ ADMIN được xóa
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}

