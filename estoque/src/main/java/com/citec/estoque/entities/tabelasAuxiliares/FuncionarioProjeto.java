package com.citec.estoque.entities.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FuncionarioProjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Projeto projeto;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Funcionario funcionario;

    //Construtores
    public FuncionarioProjeto () {}

    public FuncionarioProjeto(Projeto projeto, Funcionario funcionario) {
        this.projeto = projeto;
        this.funcionario = funcionario;
    }
}
