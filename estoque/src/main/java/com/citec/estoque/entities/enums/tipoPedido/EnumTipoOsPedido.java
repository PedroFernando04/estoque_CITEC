package com.citec.estoque.entities.enums.tipoPedido;

public enum EnumTipoOsPedido {
    ENGENHARIA ("Engenharia"),
    ELETRICA ("Elétrica"),
    ARQUITETURA ("Arquitetura"),
    OUTROS ("Outros");

    private String descricao;

    EnumTipoOsPedido(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}

}
