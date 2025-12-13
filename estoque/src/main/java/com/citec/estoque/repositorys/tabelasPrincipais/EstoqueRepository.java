package com.citec.estoque.repositorys.tabelasPrincipais;

import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByNomeIgnoreCase(String nome);
}
