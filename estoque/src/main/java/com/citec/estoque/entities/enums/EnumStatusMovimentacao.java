package com.citec.estoque.entities.enums;

public enum EnumStatusMovimentacao {
    ENTRADA ("Entrada"),
    SAIDA ("Saída"),
    MOVIMENTACAO ("Movimentação"),;

    private final String descricao;

    EnumStatusMovimentacao(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}
}
