package com.citec.estoque.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {

        model.addAttribute(
                "mensagemErro",
                ex.getMessage() != null ? ex.getMessage() : "Erro inesperado"
        );

        return "erro/telaPadrao";
    }
}