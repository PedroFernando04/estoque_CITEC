package com.citec.estoque.specification.tabelasAuxiliares;

import com.citec.estoque.entities.enums.EnumCargoFuncionario;
import com.citec.estoque.entities.enums.EnumStatusProjeto;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import org.springframework.data.jpa.domain.Specification;

public class FuncionarioProjetoSpecification {
    public static Specification<FuncionarioProjeto> comFuncionarioId(Long idFuncionario) {
        return (root, query, cb) -> {
            if (idFuncionario == null) {
                return null;
            }
            return cb.equal(root.get("funcionario").get("id"), idFuncionario);
        };
    }

    public static Specification<FuncionarioProjeto> comStatus(EnumStatusProjeto status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }
            return cb.equal(root.get("projeto").get("statusProjeto"), status);
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
