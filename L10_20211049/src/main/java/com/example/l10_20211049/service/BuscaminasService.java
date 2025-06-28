package com.example.l10_20211049.service;

import com.example.l10_20211049.entity.Configuracion;
import com.example.l10_20211049.entity.Movimiento;
import com.example.l10_20211049.repository.ConfiguracionRepository;
import com.example.l10_20211049.repository.MovimientoRepository;
import com.example.l10_20211049.repository.PosicionBombaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BuscaminasService {

    @Autowired
    private ConfiguracionRepository configuracionRepo;
    @Autowired
    private PosicionBombaRepository bombaRepo;
    @Autowired
    private MovimientoRepository movimientoRepo;

    private Set<String> bombas;
    private int errores = 0;

    @PostConstruct
    public void init() {
        bombas = bombaRepo.findAll().stream()
                .map(b -> b.getFila() + "," + b.getColumna())
                .collect(Collectors.toSet());
    }

    public String[][] generarTablero() {
        Configuracion config = configuracionRepo.findById(1L).orElseThrow();
        int filas = config.getFilas();
        int columnas = config.getColumnas();

        String[][] tablero = new String[filas][columnas];

        List<Movimiento> movs = movimientoRepo.findAll();

        for (Movimiento m : movs) {
            String key = m.getFila() + "," + m.getColumna();
            if (bombas.contains(key)) {
                if (errores == 0) {
                    tablero[m.getFila()][m.getColumna()] = "⚠️";
                    errores++;
                } else {
                    tablero[m.getFila()][m.getColumna()] = "💥";
                    errores++;
                }
            } else {
                int cantidad = contarBombasAlrededor(m.getFila(), m.getColumna());
                tablero[m.getFila()][m.getColumna()] = cantidad == 0 ? " " : String.valueOf(cantidad);
            }
        }

        return tablero;
    }

    public void guardarMovimiento(int fila, int columna) {
        Movimiento mov = new Movimiento();
        mov.setFila(fila);
        mov.setColumna(columna);
        mov.setFecha(LocalDateTime.now());
        movimientoRepo.save(mov);
    }

    public int contarBombasAlrededor(int fila, int columna) {
        int count = 0;
        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = columna - 1; j <= columna + 1; j++) {
                if (i == fila && j == columna) continue;
                if (bombas.contains(i + "," + j)) count++;
            }
        }
        return count;
    }

    public boolean juegoPerdido() {
        return errores >= 2;
    }

    public boolean juegoGanado() {
        Configuracion config = configuracionRepo.findById(1L).orElseThrow();
        int totalCasillas = config.getFilas() * config.getColumnas();
        int descubiertas = (int) movimientoRepo.count();
        return totalCasillas - descubiertas == bombas.size();
    }

    public void reiniciarJuego() {
        movimientoRepo.deleteAll();
        errores = 0;
    }
}
