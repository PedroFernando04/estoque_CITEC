package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.enums.EnumCargoFuncionario;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.repositorys.tabelasAuxiliares.FuncionarioProjetoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.FuncionarioRepository;
import com.citec.estoque.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioProjetoRepository funcionarioProjetoRepository;

    public void salvarFuncionario(Funcionario funcionario) {
        Optional<Funcionario> nomeDuplicado = funcionarioRepository.findByNomeIgnoreCase(funcionario.getNome());

        if (nomeDuplicado.isPresent()) {
            throw new IllegalArgumentException("Nome já cadastrado");
        } else
            funcionarioRepository.save(funcionario);
    }

    public void deletarFuncionario(Long funcionarioId) {
        List<FuncionarioProjeto> funcionarioProjetos = funcionarioProjetoRepository.findAll();

        if(funcionarioProjetos.isEmpty()){
            funcionarioRepository.deleteById(funcionarioId);
        } else throw new IllegalArgumentException("Funcionário atribuído a algum projeto");
    }

    public void updateFuncionario(Long id, String nome, EnumCargoFuncionario cargo) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);

        if (funcionario.isPresent()) {
            if(!nome.isBlank())
                funcionario.get().setNome(nome);
            if(cargo != null)
                funcionario.get().setCargo(cargo);

            funcionarioRepository.save(funcionario.get());
        } else throw new IllegalArgumentException("Funcionário não encontrado");
    }
}
