package com.citec.estoque.entities.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "movimentacoes")
@Getter
@Setter
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = true)
    @ManyToOne
    private Estoque origem;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Estoque destino;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Item item;

    @Column(nullable = false)
    private Integer quantidade;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime data;

    //Construtores
    public Movimentacao() {}

    public Movimentacao(Estoque origem, Estoque destino, Item item, Integer quantidade, LocalDateTime data) {
        this.origem = origem;
        this.destino = destino;
        this.item = item;
        this.quantidade = quantidade;
        this.data = data;
    }
}
