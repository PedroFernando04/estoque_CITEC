package com.citec.estoque.services;


import com.citec.estoque.entities.tabelasPrincipais.Estoque;

public interface EstoqueService {

    public void salvarEstoque(Estoque estoque);

    public void inserirItem(String itemNome, Integer quantidade, Long id);


}
