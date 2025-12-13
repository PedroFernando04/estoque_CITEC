package com.citec.estoque.repositorys.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioProjetoRepository extends JpaRepository<FuncionarioProjeto, Long> {

    List<FuncionarioProjeto> findByProjetoId(Long projetoId);
}
