package com.example.l10_20211049.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BuscaminasController {

    @Autowired
    private BuscaminasService service;

    @GetMapping("/buscaminas")
    public String verTablero(Model model) {
        String[][] tablero = service.generarTablero();
        model.addAttribute("tablero", tablero);
        model.addAttribute("perdio", service.juegoPerdido());
        model.addAttribute("gano", service.juegoGanado());
        return "buscaminas";
    }

    @PostMapping("/buscaminas/jugar")
    public String jugar(@RequestParam int fila, @RequestParam int columna) {
        service.guardarMovimiento(fila, columna);
        return "redirect:/buscaminas";
    }

    @PostMapping("/buscaminas/reiniciar")
    public String reiniciar() {
        service.reiniciarJuego();
        return "redirect:/buscaminas";
    }
}
