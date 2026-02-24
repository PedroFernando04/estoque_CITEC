package com.citec.estoque.services;

import com.citec.estoque.dtos.PedidoComItensDTO;
import com.citec.estoque.entities.enums.EnumCategoriaPedido;
import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.enums.tipoPedido.EnumTipoOsPedido;
import com.citec.estoque.entities.enums.tipoPedido.EnumTipoTransportePedido;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface PedidoService {

    public Page<PedidoComItensDTO> listarPedidosComItens(Specification<Pedido> spec, Pageable pageable);

    public void salvarPedido(Optional<Item> item, Optional<Pedido> pedido, Integer quantidade);

    public void deletarItem(Long idItemPedido);

    public void atualizarStatus(EnumStatusPedido statusPedido, Long idPedido);

    public void atualizarPedido(Long idPedido, Estoque destino, EnumStatusPedido statusPedido, String titulo, String descricao, EnumCategoriaPedido categoria, EnumTipoOsPedido tipoOs, EnumTipoTransportePedido transporte);
}
