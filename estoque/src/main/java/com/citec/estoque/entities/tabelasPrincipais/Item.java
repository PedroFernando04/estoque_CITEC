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

    @Column(nullable = true, length = 7, name = "codigo_rm")
    private String codigoRM;

    @Column(length = 6, nullable = true)
    private String codigoPatrimonio;

    //Construtores
    public Item() {}

    public Item(String nome, EnumCategoriasItem categoriaItem, String codigoRM, String codigoPatrimonio) {
        this.nome = nome;
        this.categoriaItem = categoriaItem;
        this.codigoRM = codigoRM;
        this.codigoPatrimonio = codigoPatrimonio;
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
