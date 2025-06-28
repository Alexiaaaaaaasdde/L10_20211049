package com.example.l10_20211049.repository;

import com.example.l10_20211049.entity.PosicionBomba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosicionBombaRepository extends JpaRepository<PosicionBomba, Long> {
}