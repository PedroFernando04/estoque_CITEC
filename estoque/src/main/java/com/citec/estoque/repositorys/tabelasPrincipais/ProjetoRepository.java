package com.citec.estoque.repositorys.tabelasPrincipais;

import com.citec.estoque.entities.enums.EnumStatusProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    Optional<Projeto> findByNomeIgnoreCase(String nome);

    List<Projeto> findByStatusProjeto(EnumStatusProjeto statusProjeto);

    List<Projeto> findByNomeSolicitante(String nomeSolicitante);
}
