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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuscaminasService {

    @Autowired
    private ConfiguracionRepository configuracionRepo;
    @Autowired
    private PosicionBombaRepository bombaRepo;
    @Autowired
    private MovimientoRepository movimientoRepo;

    private int errores = 0;
    private Set<String> bombas = new HashSet<>();

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

        // Obtener todas las celdas descubiertas
        Set<String> celdasDescubiertas = movimientoRepo.findAll().stream()
                .map(m -> m.getFila() + "," + m.getColumna())
                .collect(Collectors.toSet());

        // Contar errores (bombas encontradas)
        errores = (int) movimientoRepo.findAll().stream()
                .filter(m -> bombas.contains(m.getFila() + "," + m.getColumna()))
                .count();

        // Llenar el tablero
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                String key = f + "," + c;

                if (celdasDescubiertas.contains(key)) {
                    if (bombas.contains(key)) {
                        // Es una bomba descubierta
                        long bombasEncontradas = movimientoRepo.findAll().stream()
                                .filter(m -> bombas.contains(m.getFila() + "," + m.getColumna()))
                                .count();

                        if (bombasEncontradas == 1) {
                            tablero[f][c] = "⚠️"; // Primera bomba
                        } else {
                            tablero[f][c] = "💥"; // Segunda bomba
                        }
                    } else {
                        // No es bomba, mostrar número o espacio
                        int cerca = contarBombasAlrededor(f, c);
                        tablero[f][c] = cerca == 0 ? " " : String.valueOf(cerca);
                    }
                }
                // Si no está descubierta, queda null (se muestra como oculta)
            }
        }

        return tablero;
    }

    public boolean esBomba(int fila, int columna) {
        return bombas.contains(fila + "," + columna);
    }

    public void guardarMovimiento(int fila, int columna) {
        // Verificar si ya fue descubierta
        boolean yaDescubierta = movimientoRepo.findAll().stream()
                .anyMatch(m -> m.getFila() == fila && m.getColumna() == columna);

        if (yaDescubierta) {
            return; // No hacer nada si ya fue descubierta
        }

        // Guardar el movimiento inicial
        Movimiento mov = new Movimiento();
        mov.setFila(fila);
        mov.setColumna(columna);
        mov.setFecha(LocalDateTime.now());
        movimientoRepo.save(mov);

        // Si no es bomba y tiene 0 bombas alrededor, expandir
        if (!esBomba(fila, columna) && contarBombasAlrededor(fila, columna) == 0) {
            expandirCeldas(fila, columna);
        }
    }

    // Método para expandir celdas automáticamente (sin recursión)
    private void expandirCeldas(int filaInicial, int colInicial) {
        Configuracion config = configuracionRepo.findById(1L).orElseThrow();
        int filas = config.getFilas();
        int columnas = config.getColumnas();

        // Usar una cola para procesar celdas sin recursión
        Queue<int[]> cola = new LinkedList<>();
        Set<String> procesadas = new HashSet<>();

        // Obtener celdas ya descubiertas
        Set<String> yaDescubiertas = movimientoRepo.findAll().stream()
                .map(m -> m.getFila() + "," + m.getColumna())
                .collect(Collectors.toSet());

        // Agregar vecinos de la celda inicial a la cola
        for (int i = filaInicial - 1; i <= filaInicial + 1; i++) {
            for (int j = colInicial - 1; j <= colInicial + 1; j++) {
                if (i >= 0 && i < filas && j >= 0 && j < columnas) {
                    String key = i + "," + j;
                    if (!yaDescubiertas.contains(key) && !procesadas.contains(key)) {
                        cola.offer(new int[]{i, j});
                        procesadas.add(key);
                    }
                }
            }
        }

        // Procesar la cola
        while (!cola.isEmpty()) {
            int[] pos = cola.poll();
            int f = pos[0];
            int c = pos[1];
            String key = f + "," + c;

            // Si ya fue descubierta, continuar
            if (yaDescubiertas.contains(key)) {
                continue;
            }

            // Si es bomba, no descubrir
            if (esBomba(f, c)) {
                continue;
            }

            // Guardar el movimiento
            Movimiento mov = new Movimiento();
            mov.setFila(f);
            mov.setColumna(c);
            mov.setFecha(LocalDateTime.now());
            movimientoRepo.save(mov);
            yaDescubiertas.add(key);

            // Si tiene 0 bombas alrededor, agregar sus vecinos a la cola
            if (contarBombasAlrededor(f, c) == 0) {
                for (int i = f - 1; i <= f + 1; i++) {
                    for (int j = c - 1; j <= c + 1; j++) {
                        if (i >= 0 && i < filas && j >= 0 && j < columnas) {
                            String vecinoKey = i + "," + j;
                            if (!yaDescubiertas.contains(vecinoKey) && !procesadas.contains(vecinoKey)) {
                                cola.offer(new int[]{i, j});
                                procesadas.add(vecinoKey);
                            }
                        }
                    }
                }
            }
        }
    }

    public int contarBombasAlrededor(int fila, int columna) {
        Configuracion config = configuracionRepo.findById(1L).orElseThrow();
        int filas = config.getFilas();
        int columnas = config.getColumnas();

        int count = 0;
        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = columna - 1; j <= columna + 1; j++) {
                if (i == fila && j == columna) continue;
                if (i >= 0 && i < filas && j >= 0 && j < columnas) {
                    if (bombas.contains(i + "," + j)) count++;
                }
            }
        }
        return count;
    }

    public boolean juegoPerdido() {
        // Contar bombas encontradas
        long bombasEncontradas = movimientoRepo.findAll().stream()
                .filter(m -> bombas.contains(m.getFila() + "," + m.getColumna()))
                .count();
        return bombasEncontradas >= 2;
    }

    public boolean juegoGanado() {
        Configuracion config = configuracionRepo.findById(1L).orElseThrow();
        int totalCasillas = config.getFilas() * config.getColumnas();

        // Contar celdas descubiertas únicas
        Set<String> celdasDescubiertas = movimientoRepo.findAll().stream()
                .map(m -> m.getFila() + "," + m.getColumna())
                .collect(Collectors.toSet());

        int descubiertas = celdasDescubiertas.size();
        return totalCasillas - descubiertas == bombas.size();
    }

    public void reiniciarJuego() {
        movimientoRepo.deleteAll();
        errores = 0;
    }
}