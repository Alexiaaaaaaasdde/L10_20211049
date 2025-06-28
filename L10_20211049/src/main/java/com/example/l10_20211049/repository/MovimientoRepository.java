package com.example.l10_20211049.repository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findAll();
}

