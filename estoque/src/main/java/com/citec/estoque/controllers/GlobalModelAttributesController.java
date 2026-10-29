package com.citec.estoque.controllers;

import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributesController {

    @Autowired
    private ItemRepository itemRepository;

    public GlobalModelAttributesController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @ModelAttribute("quantidadeFaltando")
    public Integer getQuantidadeFaltando() {
        return itemRepository.countByFaltandoTrue();
    }
}
