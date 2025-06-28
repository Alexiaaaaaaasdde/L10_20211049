package com.example.l10_20211049.entity;

import java.time.LocalDateTime;

@Entity
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int fila;
    private int columna;
    private LocalDateTime fecha;
}
