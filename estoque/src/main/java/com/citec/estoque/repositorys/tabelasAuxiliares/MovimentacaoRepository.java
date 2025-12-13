package com.citec.estoque.repositorys.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasAuxiliares.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByOrigemId(Long id);

    List<Movimentacao> findByDestinoId(Long id);
}
