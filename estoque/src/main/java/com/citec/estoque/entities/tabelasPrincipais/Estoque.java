package com.citec.estoque.entities.tabelasPrincipais;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Locais")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ESTOQUE")
@Getter
@Setter
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    //Construtores
    public Estoque() {}

    public Estoque(String nome) {
        this.nome = nome;
    }

    //Setter
    @Transient
    public String getTipo() {
        return this.getClass().getSimpleName();
    }

}
