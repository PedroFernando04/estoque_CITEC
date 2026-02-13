package com.citec.estoque.specification.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import org.springframework.data.jpa.domain.Specification;

public class ItemEstoqueSpecification {

    public static Specification<ItemEstoque> comNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) {
                return null;
            }
            return cb.or(
                    cb.like(cb.lower(root.get("item").get("nome")), "%" + nome.toLowerCase() + "%"),
                    cb.like((root.get("item").get("codigoRM")), "%" +  nome + "%"),
                    cb.like((root.get("item").get("codigoPatrimonio")), "%" +  nome + "%")
            );
        };
    }

    public static Specification<ItemEstoque> comEstoque(Long estoqueId) {
        return (root, query, cb) -> {
            if (estoqueId == null) {
                return null;
            }
            return cb.equal(root.get("estoque").get("id"), estoqueId);
        };
    }

    public static Specification<ItemEstoque> comFaltando(Boolean faltando) {
        return (root, query, cb) -> {
            if (faltando == null) {
                return null;
            }
            return cb.equal(root.get("faltando"), faltando);
        };
    }

}


