package com.citec.estoque.entities.enums;

public enum EnumCategoriaPedido {

    RM ("Requisição de Material"),
    OS ("Ordem de Serviço"),
    TRANSPORTE ("Transporte");

    private String descricao;

    EnumCategoriaPedido(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}
}
