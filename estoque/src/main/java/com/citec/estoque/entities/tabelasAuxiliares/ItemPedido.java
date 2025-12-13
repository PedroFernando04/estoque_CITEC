package com.citec.estoque.entities.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne()
    private Pedido pedido;

    @JoinColumn(nullable = false)
    @ManyToOne()
    private Item item;

    @Column(nullable = false)
    private Integer quantidade;

    //Construtores
    public ItemPedido() {}

    public ItemPedido(Pedido pedido, Item item, Integer quantidade) {
        this.pedido = pedido;
        this.item = item;
        this.quantidade = quantidade;
    }
}
