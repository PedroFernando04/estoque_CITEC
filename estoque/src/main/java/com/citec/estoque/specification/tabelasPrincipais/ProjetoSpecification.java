package com.citec.estoque.specification.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumCategoriaProjeto;
import com.citec.estoque.entities.enums.EnumStatusProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import org.springframework.data.jpa.domain.Specification;

public class ProjetoSpecification {
    public static Specification<Projeto> comNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nome")), "%" + nome + "%");
        };
    }

    public static Specification<Projeto> comSolicitante(String solicitante) {
        return (root, query, cb) -> {
            if (solicitante == null || solicitante.isBlank()) {
                return null;
            }

            return cb.equal(root.get("nomeSolicitante"), solicitante);
        };
    }

    public static Specification<Projeto> comStatus(EnumStatusProjeto status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }
            return cb.equal(root.get("statusProjeto"), status);
        };
    }

    public static Specification<Projeto> comCategoria(EnumCategoriaProjeto categoria) {
        return (root, query, cb) -> {
            if (categoria == null) {
                return null;
            }
            return cb.equal(root.get("categoria"), categoria);
        };
    }

}
