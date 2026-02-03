package com.citec.estoque.specification.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class PedidoSpecification {

    public static Specification<Pedido> comStatus(EnumStatusPedido status) {
        return (root, criteriaQuery, cb) -> {
            if (status == null) {
                return null;
            }
            return cb.equal(root.get("statusPedido"), status);
        };
    }

    public static Specification<Pedido> comDestino(Long destinoId) {
        return (root, criteriaQuery, cb) -> {
            if (destinoId == null) {
                return null;
            }
            return cb.equal(root.get("destino").get("id"), destinoId);
        };
    }

    public static Specification<Pedido> comItem(Long itemId) {
        return (root, query, cb) -> {

            if (itemId == null) {
                return cb.conjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ItemPedido> itemRoot = subquery.from(ItemPedido.class);

            subquery.select(itemRoot.get("pedido").get("id"))
                    .where(cb.equal(
                            itemRoot.get("item").get("id"),
                            itemId
                    ));

            return root.get("id").in(subquery);
        };
    }


}
