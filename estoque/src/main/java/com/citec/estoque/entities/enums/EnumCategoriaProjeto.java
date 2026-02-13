package com.citec.estoque.entities.enums;

public enum EnumCategoriaProjeto {
    AULA("Aula"),
    MPP("MPPs"),
    PROJETO("Projeto"),
    CITEC("");

    private String descricao;

    EnumCategoriaProjeto(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}
}
