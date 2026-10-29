package com.citec.estoque.specification.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumCargoFuncionario;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import org.springframework.data.jpa.domain.Specification;

public class FuncionarioSpecification {
    public static Specification<Funcionario> comNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null ||  nome.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nome")), "%" + nome + "%");
        };
    }

    public static Specification<Funcionario> comCargo(EnumCargoFuncionario cargo) {
        return (root, query, cb) -> {
            if (cargo == null) {
                return null;
            }
            return  cb.equal(root.get("cargo"), cargo);
        };
    }
}
