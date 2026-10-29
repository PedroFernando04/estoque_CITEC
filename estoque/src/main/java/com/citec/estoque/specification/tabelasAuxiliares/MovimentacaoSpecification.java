package com.citec.estoque.specification.tabelasAuxiliares;

import com.citec.estoque.entities.enums.EnumStatusMovimentacao;
import com.citec.estoque.entities.tabelasAuxiliares.Movimentacao;
import org.springframework.data.jpa.domain.Specification;

public class MovimentacaoSpecification {
    public static Specification<Movimentacao> comStatus(EnumStatusMovimentacao status){
        return (root, query, cb) -> {
            if (status == null){
                return null;
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Movimentacao> comOrigem(Long origemId){
        return (root, query, cb) -> {
            if (origemId == null){
                return null;
            }
            return cb.equal(root.get("origem").get("id"), origemId);
        };
    }

    public static Specification<Movimentacao> comDestino(Long destinoId){
        return (root, query, cb) -> {
            if (destinoId == null){
                return null;
            }
            return cb.equal(root.get("destino").get("id"), destinoId);
        };
    }

    public static Specification<Movimentacao> comItem(Long itemId){
        return (root, query, cb) -> {
            if (itemId == null){
                return null;
            }
            return cb.equal(root.get("item").get("id"), itemId);
        };
    }
}
