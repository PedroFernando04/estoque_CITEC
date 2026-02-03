package com.citec.estoque.specification.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumStatusProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import org.springframework.data.jpa.domain.Specification;

public class EstoqueSpecification {
    public static Specification<Estoque> comNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nome")), "%" + nome + "%");
        };
    }

    public static Specification<Estoque> comSolicitante(String solicitante) {
        return (root, query, cb) -> {
            if (solicitante == null || solicitante.isBlank()) {
                return null;
            }

            return cb.equal(cb.treat(root, Projeto.class).get("nomeSolicitante"), solicitante);
        };
    }

    public static Specification<Estoque> comStatus(EnumStatusProjeto status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }
            return cb.equal(cb.treat(root, Projeto.class).get("statusProjeto"), status);
        };
    }

    public static Specification<Estoque> comTipo(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null || tipo.isBlank()) {
                return null;
            }

            // Com o InheritanceType.JOINED, o JPA sabe
            // qual subclasse usar baseado no tipo da entidade
            if ("PROJETO".equalsIgnoreCase(tipo)) {
                return cb.equal(root.type(), Projeto.class);
            } else if ("ESTOQUE".equalsIgnoreCase(tipo)) {
                return cb.equal(root.type(), Estoque.class);
            }

            return null;
        };
    }

}
