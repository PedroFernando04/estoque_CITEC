package com.citec.estoque.entities.enums.tipoPedido;

public enum EnumTipoTransportePedido {
    CARRO ("Carro"),
    VAN ("Van"),
    CAMINHAO_BAU ("Caminhão Baú"),
    CAMINHONETE ("Caminhonete");

    private String descricao;

    EnumTipoTransportePedido(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}

}
