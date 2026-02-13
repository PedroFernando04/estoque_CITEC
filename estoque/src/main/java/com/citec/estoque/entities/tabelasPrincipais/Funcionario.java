package com.citec.estoque.entities.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumCargoFuncionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "funcionarios")
@Getter
@Setter
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column()
    @Enumerated(EnumType.STRING)
    private EnumCargoFuncionario cargo;

    @Column(nullable = true, unique = true)
    private String login;

    @Column(nullable = true, unique = true)
    private String senha;

    //Construtores
    public Funcionario() {}

    public Funcionario(Long id, String nome, EnumCargoFuncionario cargo, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
    }
}
