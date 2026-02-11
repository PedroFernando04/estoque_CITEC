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

    //Construtores
    public Funcionario() {}

    public Funcionario(String nome, EnumCargoFuncionario cargo) {
        this.nome = nome;
        this.cargo = cargo;
    }
}
