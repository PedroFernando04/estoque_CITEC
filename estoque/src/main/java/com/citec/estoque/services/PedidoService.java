package com.citec.estoque.services;

import com.citec.estoque.dtos.pedidos.PedidoComItensDTO;

import java.util.List;

public interface PedidoService {

    public List<PedidoComItensDTO> listarPedidosComItens();
}
