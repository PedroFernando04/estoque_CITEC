package com.citec.estoque.entities.enums;

public enum EnumCategoriasItem {
    ELETRONICA ("Eletrônica"),
    MECANICA ("Mecânica"),
    MARCENARIA ("Marcenaria");

    private final String descricao;

    EnumCategoriasItem(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}
}
