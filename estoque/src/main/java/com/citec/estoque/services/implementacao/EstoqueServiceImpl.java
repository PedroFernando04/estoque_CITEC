package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.repositorys.tabelasPrincipais.EstoqueRepository;
import com.citec.estoque.services.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class EstoqueServiceImpl implements EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    //CREATE
    public void salvarEstoque(Estoque estoque) {
        Optional<Estoque> estoqueRetorno = estoqueRepository.findByNomeIgnoreCase(estoque.getNome());

        if (estoqueRetorno.isPresent())
            throw new IllegalArgumentException("Nome Duplicado");

        else
            estoqueRepository.save(estoque);
    }


}
