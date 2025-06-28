package com.example.l10_20211049.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "posicionbomba")
public class PosicionBomba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int fila;
    private int columna;

    // Constructores
    public PosicionBomba() {}

    public PosicionBomba(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }

    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna; }
}