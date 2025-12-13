package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import com.citec.estoque.repositorys.tabelasPrincipais.ProjetoRepository;
import com.citec.estoque.services.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjetoServiceImpl implements ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;


    public void salvarProjeto(Projeto projeto) {
        Optional<Projeto> projetoRetorno = projetoRepository.findByNomeIgnoreCase(projeto.getNome());

        if (projetoRetorno.isPresent())
            throw new IllegalArgumentException("Nome Duplicado");

        else
            projetoRepository.save(projeto);
    }
}
