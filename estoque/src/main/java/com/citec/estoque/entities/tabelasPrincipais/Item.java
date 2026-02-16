package com.citec.estoque.entities.tabelasPrincipais;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "itens")
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String nome;

    @Column(nullable = false, unique = true, name = "nome_fantasia")
    private String nomeFantasia;

    @Column(nullable = true, length = 7, name = "codigo_rm")
    private String codigoRM;

    @Column(nullable = true)
    private String imagePath;

    @Column(nullable = false)
    private Boolean faltando = false;

    //Construtores
    public Item() {}

    public Item(Long id, String nome, String nomeFantasia, String codigoRM, String imagePath,  Boolean faltando) {
        this.id = id;
        this.nome = nome;
        this.nomeFantasia = nomeFantasia;
        this.codigoRM = codigoRM;
        this.imagePath = imagePath;
        this.faltando = faltando;
    }

    //Getter e Setter
    public String getCodigoRMFormatado() {

        if (this.codigoRM == null || this.codigoRM.length() != 7) {
            return this.codigoRM;
        }

        return this.codigoRM.replaceAll(
                "(\\d{2})(\\d{2})(\\d{3})",
                "$1.$2.$3"
        );
    }

}
