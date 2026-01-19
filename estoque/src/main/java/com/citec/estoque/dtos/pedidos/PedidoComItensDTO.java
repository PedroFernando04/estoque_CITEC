package com.citec.estoque.dtos.pedidos;

import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoComItensDTO {

    private Long id;
    private LocalDateTime data;
    private EnumStatusPedido status;
    private List<ItemPedido> itens;
    private Estoque destino;

    //Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public EnumStatusPedido getStatus() {
        return status;
    }

    public void setStatus(EnumStatusPedido status) {
        this.status = status;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public Estoque getDestino() {
        return destino;
    }

    public void setDestino(Estoque destino) {
        this.destino = destino;
    }
}
