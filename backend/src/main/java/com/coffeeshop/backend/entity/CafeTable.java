package com.coffeeshop.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cafe_tables")
public class CafeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tableNumber;
    private String description;

    @Enumerated(EnumType.STRING)
    private TableStatus status;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getDescription() {
        return description;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
}
