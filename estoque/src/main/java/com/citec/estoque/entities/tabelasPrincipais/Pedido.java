package com.citec.estoque.entities.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumCategoriaPedido;
import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.enums.tipoPedido.EnumTipoOsPedido;
import com.citec.estoque.entities.enums.tipoPedido.EnumTipoTransportePedido;
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

    @Column
    private String descricao;

    @Column
    private String titulo;

    @Column
    private EnumCategoriaPedido  categoria;

    @Column
    private EnumTipoOsPedido tipoOs;

    @Column
    private EnumTipoTransportePedido tipoTransporte;

    //Construtores
    public Pedido() {}

    public Pedido(Long id, LocalDateTime dataPedido, EnumStatusPedido statusPedido, Estoque destino, String descricao, String titulo, EnumCategoriaPedido categoria, EnumTipoOsPedido tipoOs, EnumTipoTransportePedido tipoTransporte) {
        this.id = id;
        this.dataPedido = dataPedido;
        this.statusPedido = statusPedido;
        this.destino = destino;
        this.descricao = descricao;
        this.titulo = titulo;
        this.categoria = categoria;
        this.tipoOs = tipoOs;
        this.tipoTransporte = tipoTransporte;

    }

    //Getter
    public String getClasseLabelStatus(){
        return switch (getStatusPedido()) {
            case SOLICITADO -> "card-label-planejado";
            case RECEBIDO -> "card-label-concluido";
            case CANCELADO -> "card-label-cancelado";
        };
    }

    public String getClasseLabelCategoria(){
        return switch (getCategoria()) {
            case RM -> "card-label-rm";
            case OS -> "card-label-os";
            case TRANSPORTE -> "card-label-transporte";
        };
    }
}
