package com.citec.estoque.entities.enums;

public enum EnumCleroFuncionario {
    PAPA ("Papa"),
    CARDEAL ("Cardeal"),
    BISPO ("Bispo"),
    PADRE ("Padre"),
    COROINHA ("Coroinha");

    private String descricao;

    EnumCleroFuncionario(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}
}
