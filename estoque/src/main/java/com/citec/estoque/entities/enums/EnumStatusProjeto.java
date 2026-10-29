package com.citec.estoque.entities.enums;

public enum EnumStatusProjeto {
    PLANEJADO ("Planejado"),
    EM_ANDAMENTO ("Em andamento"),
    CONCLUIDO ("Concluído"),
    CANCELADO ("Cancelado"),;

    private final String descricao;

    EnumStatusProjeto(String descricao) {this.descricao = descricao;}

    public String getDescricao() {return descricao;}

}
