package com.citec.estoque.services;


import com.citec.estoque.entities.tabelasPrincipais.Estoque;

public interface EstoqueService {

    public void salvarEstoque(Estoque estoque);

    public void inserirItem(String itemNome, Integer quantidade, Long id, String itemOrigem);

    public void deletarItemEstoque(Long itemEstoqueId, Integer quantidade);

    public void atualizarEstoque(Long id, String nome);


}
