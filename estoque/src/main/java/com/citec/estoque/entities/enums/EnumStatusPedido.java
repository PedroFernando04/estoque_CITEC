package com.citec.estoque.entities.enums;

public enum EnumStatusPedido {
    SOLICITADO ("Solicitado"),
    RECEBIDO ("Recebido"),
    CANCELADO ("Cancelado"),;

    private final String descricao;

    EnumStatusPedido(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}
}
