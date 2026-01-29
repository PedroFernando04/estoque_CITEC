package com.citec.estoque.entities.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumStatusProjeto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("PROJETO")
@Getter
@Setter
public class Projeto extends Estoque {

    @Column(nullable = false)
    private String nomeSolicitante;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumStatusProjeto  statusProjeto;

    //Construtores
    public Projeto() {}

    public Projeto(String nome, String nomeSolicitante, EnumStatusProjeto statusProjeto) {
        super(nome);
        this.nomeSolicitante = nomeSolicitante;
        this.statusProjeto = statusProjeto;
    }

    //Getter
    public String getClasseLabelStatus(){
        return switch (getStatusProjeto()) {
            case PLANEJADO -> "card-label-planejado";
            case EM_ANDAMENTO -> "card-label-andamento";
            case CONCLUIDO -> "card-label-concluido";
            case CANCELADO -> "card-label-cancelado";
            };
        }
    }
