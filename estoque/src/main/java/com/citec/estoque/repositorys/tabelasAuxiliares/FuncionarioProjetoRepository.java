package com.citec.estoque.repositorys.tabelasAuxiliares;

import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioProjetoRepository extends JpaRepository<FuncionarioProjeto, Long>, JpaSpecificationExecutor<FuncionarioProjeto> {

    List<FuncionarioProjeto> findByProjetoId(Long projetoId);
}
