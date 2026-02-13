package com.citec.estoque.services;

import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Item;

public interface ItemService {

    public String limparRm(String rm);

    public void salvarItem(Item item);

    public void removerItem(Item item);

    public void updateItem(Long id, String nome, String rm);

    public void adicionarUmItemEstoque(Long item);

    public void removerUmItemEstoque(Long item);

    public void adicionarUmItemPedido(Long item);

    public void removerUmItemPedido(Long item);
}
