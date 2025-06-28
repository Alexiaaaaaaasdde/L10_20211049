package com.example.l10_20211049.entity;

@Entity
public class PosicionBomba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int fila;
    private int columna;

}
