package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.repositorys.tabelasPrincipais.FuncionarioRepository;
import com.citec.estoque.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;


    public void salvarFuncionario(Funcionario funcionario) {
        Optional<Funcionario> nomeDuplicado = funcionarioRepository.findByNomeIgnoreCase(funcionario.getNome());

        if (nomeDuplicado.isPresent()) {
            throw new IllegalArgumentException("Nome já cadastrado");
        } else
            funcionarioRepository.save(funcionario);
    }
}
