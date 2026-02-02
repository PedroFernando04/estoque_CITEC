package com.citec.estoque.specification.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumCleroFuncionario;
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

    public static Specification<Funcionario> comClero(EnumCleroFuncionario clero) {
        return (root, query, cb) -> {
            if (clero == null) {
                return null;
            }
            return  cb.equal(root.get("clero"), clero);
        };
    }
}
