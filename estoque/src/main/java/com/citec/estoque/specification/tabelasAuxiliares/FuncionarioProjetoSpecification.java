package com.citec.estoque.specification.tabelasAuxiliares;

import com.citec.estoque.entities.enums.EnumCargoFuncionario;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import org.springframework.data.jpa.domain.Specification;

public class FuncionarioProjetoSpecification {
    public static Specification<FuncionarioProjeto> comNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("funcionario").get("nome")), "%" + nome + "%");
        };
    }

    public static Specification<FuncionarioProjeto> comClero(EnumCargoFuncionario clero) {
        return (root, query, cb) -> {
            if (clero == null) {
                return null;
            }
            return cb.equal(root.get("funcionario").get("clero"), clero);
        };
    }

    public static Specification<FuncionarioProjeto> comProjeto(Long projeto) {
        return (root, query, cb) ->  {
            if (projeto == null) {
                return null;
            }
            return  cb.equal(root.get("projeto").get("id"), projeto);
        };
    }
}
