package com.citec.estoque.services;

import com.citec.estoque.dtos.pedidos.PedidoComItensDTO;
import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    public List<PedidoComItensDTO> listarPedidosComItens();

    public void salvarPedido(Optional<Item> item, Optional<Pedido> pedido, Integer quantidade);

    public void deletarItem(Long idItemPedido);

    public void atualizarStatus(EnumStatusPedido statusPedido, Long idPedido);
}
