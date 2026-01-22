package com.citec.estoque.services;

import com.citec.estoque.entities.enums.EnumCategoriasItem;
import com.citec.estoque.entities.tabelasPrincipais.Item;

public interface ItemService {

    public void salvarItem(Item item);

    public void removerItem(Item item);

    public void updateItem(Long id, String nome, EnumCategoriasItem categoria, String rm, String patrimonio);
}
