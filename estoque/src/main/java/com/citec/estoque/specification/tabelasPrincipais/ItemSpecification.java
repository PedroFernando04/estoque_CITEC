package com.citec.estoque.specification.tabelasPrincipais;

import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {

    public static Specification<Item> comNome(String nome){
        return (root, query, cb) -> {
            if(nome == null || nome.isBlank()){
                return null;
            }
            return cb.equal(cb.lower(root.get("nome")), "%" +  nome + "%");
        };
    }

    public static Specification<Item> comNomeFantasia(String nomeFantasia){
        return (root, query, cb) -> {
            if(nomeFantasia == null || nomeFantasia.isBlank()){
                return null;
            }
            return cb.equal(cb.lower(root.get("nomeFantasia")), "%" +  nomeFantasia + "%");
        };
    }

    public static Specification<Item> comCodigoRM(String codigoRM){
        return (root, query, cb) -> {
            if(codigoRM == null || codigoRM.isBlank()){
                return null;
            }
            return cb.equal(cb.lower(root.get("codigoRM")), "%" +  codigoRM + "%");
        };
    }

    public static Specification<Item> buscaGlobal(String termo) {
        return (root, query, cb) -> {
            if (termo == null || termo.isBlank()) {
                return null;
            }

            // Transformamos em minúsculo e adicionamos os coringas %
            String termoLike = "%" + termo.toLowerCase() + "%";

            // Criamos as condições (Predicates) usando LIKE
            var predicateNome = cb.like(cb.lower(root.get("nome")), termoLike);
            var predicateNomeFantasia = cb.like(cb.lower(root.get("nomeFantasia")), termoLike);
            var predicateCodigoRM = cb.like(cb.lower(root.get("codigoRM")), termoLike);

            // Une as três condições com um "OR"
            return cb.or(predicateNome, predicateNomeFantasia, predicateCodigoRM);
        };
    }

    public static Specification<Item> comFaltando(Boolean faltando) {
        return (root, query, cb) -> {
            if (faltando == null) {
                return null;
            }
            return cb.equal(root.get("faltando"), faltando);
        };
    }
}
