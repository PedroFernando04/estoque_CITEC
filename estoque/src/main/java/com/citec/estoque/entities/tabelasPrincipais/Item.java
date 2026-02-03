package com.citec.estoque.entities.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumCategoriasItem;
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

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumCategoriasItem categoriaItem;

    @Column(nullable = true, length = 7, name = "codigo_rm", unique = true)
    private String codigoRM;

    @Column(length = 6, nullable = true, unique = true)
    private String codigoPatrimonio;

    //Construtores
    public Item() {}

    public Item(String nome, EnumCategoriasItem categoriaItem, String codigoRM, String codigoPatrimonio) {
        this.nome = nome;
        this.categoriaItem = categoriaItem;
        this.codigoRM = codigoRM;
        this.codigoPatrimonio = codigoPatrimonio;
    }

}
