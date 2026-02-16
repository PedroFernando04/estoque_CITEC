package com.citec.estoque.services.implementacao;

import com.citec.estoque.dtos.PedidoComItensDTO;
import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemPedidoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.PedidoRepository;
import com.citec.estoque.services.EstoqueService;
import com.citec.estoque.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private EstoqueService estoqueService;

    public Page<PedidoComItensDTO> listarPedidosComItens(Specification<Pedido> spec, Pageable pageable) {

        Page<Pedido> paginaPedidos = pedidoRepository.findAll(spec, pageable);

        List<PedidoComItensDTO> dtos = paginaPedidos.getContent()
                .stream()
                .map(pedido -> {

                    List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);

                    PedidoComItensDTO dto = new PedidoComItensDTO();
                    dto.setId(pedido.getId());
                    dto.setData(pedido.getDataPedido());
                    dto.setStatus(pedido.getStatusPedido());
                    dto.setDestino(pedido.getDestino());
                    dto.setDescricao(pedido.getDescricao());
                    dto.setTitulo(pedido.getTitulo());
                    dto.setCategoria(pedido.getCategoria());
                    dto.setTipoOs(pedido.getTipoOs());
                    dto.setTipoTransporte(pedido.getTipoTransporte());

                    dto.setItens(itens);

                    return dto;
                })
                .toList();

        return new PageImpl<>(
                dtos,
                pageable,
                paginaPedidos.getTotalElements()
        );
    }

    public void salvarPedido(Optional<Item> item, Optional<Pedido> pedido, Integer quantidade) {
        if (item.isPresent() && pedido.isPresent()) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(quantidade);
            itemPedido.setItem(item.get());
            itemPedido.setPedido(pedido.get());

            itemPedidoRepository.save(itemPedido);
        } else throw new IllegalArgumentException("Item não encontrado");
    }

    public void deletarItem(Long idItemPedido){
        itemPedidoRepository.deleteById(idItemPedido);
    }

    public void atualizarStatus(EnumStatusPedido statusPedido, Long idPedido){
        Optional<Pedido> pedido = pedidoRepository.findById(idPedido);

        if (pedido.isPresent()) {
            pedido.get().setStatusPedido(statusPedido);
            pedidoRepository.save(pedido.get());
        } else throw new IllegalArgumentException("Erro ao atualizar Status Pedido");

        if (statusPedido.equals(EnumStatusPedido.RECEBIDO)){
            List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido.get());

            for (ItemPedido itemPedido : itens) {
                estoqueService.inserirItem(itemPedido.getItem().getNome(), itemPedido.getQuantidade(), itemPedido.getPedido().getDestino().getId(), null);
            }

        }

    }
}
