package com.citec.estoque.repositorys.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasAuxiliares.Movimentacao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long>, JpaSpecificationExecutor<Movimentacao> {

    List<Movimentacao> findByDestinoIdAndItemId(Long id, Long itemId);

    List<Movimentacao> findByItemId(Long id);

    @Query("SELECT m FROM movimentacoes m WHERE m.origem.id = :id OR m.destino.id = :id ORDER BY m.data DESC")
    List<Movimentacao> findTop15ByOrigemOrDestino(Long id, Pageable pageable);
}
