package com.citec.estoque.entities.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ItemEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Estoque estoque;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Item item;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Boolean faltando = false;

    //Construtores
    public ItemEstoque() {}

    public ItemEstoque(Estoque estoque, Item item, Integer quantidade, Boolean faltando) {
        this.estoque = estoque;
        this.item = item;
        this.quantidade = quantidade;
        this.faltando = faltando;
    }
}
