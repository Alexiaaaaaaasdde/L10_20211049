package com.example.l10_20211049.controller;

import com.example.l10_20211049.service.BuscaminasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/buscaminas")
public class BuscaminasController {

    @Autowired
    private BuscaminasService buscaminasService;

    @GetMapping("")
    public String mostrarJuego(Model model) {
        // Generar el tablero actual
        String[][] tablero = buscaminasService.generarTablero();

        // Verificar estado del juego
        boolean perdio = buscaminasService.juegoPerdido();
        boolean gano = buscaminasService.juegoGanado();

        // Agregar atributos al modelo
        model.addAttribute("tablero", tablero);
        model.addAttribute("perdio", perdio);
        model.addAttribute("gano", gano);

        return "buscaminas";
    }

    @PostMapping("/jugar")
    public String jugar(@RequestParam int fila, @RequestParam int columna) {
        // Verificar que el juego no haya terminado
        if (!buscaminasService.juegoPerdido() && !buscaminasService.juegoGanado()) {
            buscaminasService.guardarMovimiento(fila, columna);
        }

        return "redirect:/buscaminas";
    }

    @PostMapping("/reiniciar")
    public String reiniciar() {
        buscaminasService.reiniciarJuego();
        return "redirect:/buscaminas";
    }
}