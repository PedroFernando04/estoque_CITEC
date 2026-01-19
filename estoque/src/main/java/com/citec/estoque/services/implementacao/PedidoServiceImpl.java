package com.citec.estoque.services.implementacao;

import com.citec.estoque.dtos.pedidos.PedidoComItensDTO;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemPedidoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.PedidoRepository;
import com.citec.estoque.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public List<PedidoComItensDTO> listarPedidosComItens() {

        List<Pedido> pedidos = pedidoRepository.findAll();
        List<PedidoComItensDTO> pedidosComItensDTO = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);

            PedidoComItensDTO dto = new PedidoComItensDTO();
            dto.setId(pedido.getId());
            dto.setData(pedido.getDataPedido());
            dto.setStatus(pedido.getStatusPedido());
            dto.setItens(itens);
            dto.setDestino(pedido.getDestino());

            pedidosComItensDTO.add(dto);
        }

        return pedidosComItensDTO;
    }
}
