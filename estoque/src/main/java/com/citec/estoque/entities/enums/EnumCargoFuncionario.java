package com.citec.estoque.entities.enums;

public enum EnumCargoFuncionario {
    EFETIVO("Efetivo"),
    ESTAGIARIO("Estagiário"),
    COORDENADOR("Coordenador");

    private String descricao;

    EnumCargoFuncionario(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}
}
