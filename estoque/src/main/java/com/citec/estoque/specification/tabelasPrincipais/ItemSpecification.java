package com.citec.estoque.specification.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumCategoriasItem;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {

    public static Specification<Item> comCategoria(EnumCategoriasItem categoria){
        return (root, query, cb) -> {
            if(categoria == null){
                return null;
            }
            return cb.equal(root.get("categoriaItem"), categoria);
        };

    }

    public static Specification<Item> comNome(String nome){
        return (root, query, cb) -> {
            if(nome == null || nome.isBlank()){
                return null;
            }
            return cb.equal(cb.lower(root.get("nome")), "%" +  nome + "%");
        };
    }
}
