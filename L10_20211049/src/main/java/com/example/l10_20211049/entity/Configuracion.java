package com.example.l10_20211049.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "configuracion")
public class Configuracion {
    @Id
    private Long id;
    private int filas;
    private int columnas;
    private int cantidadBombas;

    // Constructores
    public Configuracion() {}

    public Configuracion(Long id, int filas, int columnas, int cantidadBombas) {
        this.id = id;
        this.filas = filas;
        this.columnas = columnas;
        this.cantidadBombas = cantidadBombas;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getFilas() { return filas; }
    public void setFilas(int filas) { this.filas = filas; }

    public int getColumnas() { return columnas; }
    public void setColumnas(int columnas) { this.columnas = columnas; }

    public int getCantidadBombas() { return cantidadBombas; }
    public void setCantidadBombas(int cantidadBombas) { this.cantidadBombas = cantidadBombas; }
}