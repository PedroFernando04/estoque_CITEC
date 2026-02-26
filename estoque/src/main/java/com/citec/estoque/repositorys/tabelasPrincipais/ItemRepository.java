package com.citec.estoque.repositorys.tabelasPrincipais;

import com.citec.estoque.entities.tabelasPrincipais.Item;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    Optional<Item> findByNomeIgnoreCase (String nome);

    Optional<Item> findByCodigoRM(String codigoRM);

    Optional<Item> findByNomeFantasiaIgnoreCase(String nomeFantasia);

    Integer countByFaltandoTrue();
}
