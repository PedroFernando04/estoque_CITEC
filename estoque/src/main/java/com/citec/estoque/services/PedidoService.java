package com.citec.estoque.services;

import com.citec.estoque.dtos.pedidos.PedidoComItensDTO;
import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    public Page<PedidoComItensDTO> listarPedidosComItens(Specification<Pedido> spec, Pageable pageable);

    public void salvarPedido(Optional<Item> item, Optional<Pedido> pedido, Integer quantidade);

    public void deletarItem(Long idItemPedido);

    public void atualizarStatus(EnumStatusPedido statusPedido, Long idPedido);
}
