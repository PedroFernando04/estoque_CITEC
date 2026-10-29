package com.citec.estoque.dtos;

import com.citec.estoque.entities.enums.EnumCategoriaPedido;
import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.enums.tipoPedido.EnumTipoOsPedido;
import com.citec.estoque.entities.enums.tipoPedido.EnumTipoTransportePedido;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoComItensDTO {

    private Long id;
    private LocalDateTime data;
    private EnumStatusPedido status;
    private String descricao;
    private String titulo;
    private EnumCategoriaPedido categoria;
    private EnumTipoOsPedido tipoOs;
    private EnumTipoTransportePedido tipoTransporte;
    private Estoque destino;

    private List<ItemPedido> itens;

    //Getters e Setters

    public String getClasseLabelStatus(){
        return switch (getStatus()) {
            case SOLICITADO -> "card-label-planejado";
            case RECEBIDO -> "card-label-concluido";
            case CANCELADO -> "card-label-cancelado";
        };
    }

    public String getClasseLabelCategoria(){
        return switch (getCategoria()) {
            case RM -> "card-label-rm";
            case OS -> "card-label-os";
            case TRANSPORTE -> "card-label-transporte";
        };
    }
}
