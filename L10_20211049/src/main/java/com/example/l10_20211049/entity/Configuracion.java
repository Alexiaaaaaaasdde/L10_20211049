package com.example.l10_20211049.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Configuracion {
    @Id
    private Long id;
    private int filas;
    private int columnas;
    private int cantidadBombas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getCantidadBombas() {
        return cantidadBombas;
    }

    public void setCantidadBombas(int cantidadBombas) {
        this.cantidadBombas = cantidadBombas;
    }
}

