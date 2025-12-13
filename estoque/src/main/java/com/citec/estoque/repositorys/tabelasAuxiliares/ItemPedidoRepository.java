package com.citec.estoque.repositorys.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
