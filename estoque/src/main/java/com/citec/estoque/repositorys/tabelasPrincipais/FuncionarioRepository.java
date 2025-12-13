package com.citec.estoque.repositorys.tabelasPrincipais;

import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByNomeIgnoreCase(String nome);

}
