package com.citec.estoque.entities.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumCleroFuncionario;
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
    private EnumCleroFuncionario clero;

    //Construtores
    public Funcionario() {}

    public Funcionario(String nome, EnumCleroFuncionario clero) {
        this.nome = nome;
        this.clero = clero;
    }
}
