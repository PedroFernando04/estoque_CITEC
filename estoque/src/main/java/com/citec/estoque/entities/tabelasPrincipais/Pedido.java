package com.citec.estoque.entities.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumStatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "pedidos")
@Getter
@Setter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime dataPedido;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumStatusPedido statusPedido;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Estoque destino;

    //Construtores
    public Pedido() {}

    public Pedido(LocalDateTime dataPedido, EnumStatusPedido statusPedido, Estoque destino) {
        this.dataPedido = dataPedido;
        this.statusPedido = statusPedido;
        this.destino = destino;
    }

    //Getter
    public String getClasseLabelStatus(){
        return switch (getStatusPedido()) {
            case SOLICITADO -> "card-label-planejado";
            case RECEBIDO -> "card-label-concluido";
            case CANCELADO -> "card-label-cancelado";
        };
    }
}
