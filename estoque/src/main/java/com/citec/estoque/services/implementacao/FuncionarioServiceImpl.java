package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.enums.EnumCargoFuncionario;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.repositorys.tabelasAuxiliares.FuncionarioProjetoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.FuncionarioRepository;
import com.citec.estoque.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioProjetoRepository funcionarioProjetoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void salvarFuncionario(Funcionario funcionario, String senha) {
        Optional<Funcionario> nomeDuplicado = funcionarioRepository.findByNomeIgnoreCase(funcionario.getNome());

        if (nomeDuplicado.isPresent()) {
            throw new IllegalArgumentException("Nome já cadastrado");
        } else{
            funcionario.setSenha(passwordEncoder.encode(senha));
            funcionarioRepository.save(funcionario);
        }

    }

    public boolean verifyPassword(String senhaDigitada, String senhaCriptografada) {
        return passwordEncoder.matches(senhaDigitada,senhaCriptografada);
    }

    public Funcionario getFuncionarioByLogin(String login) {
        return funcionarioRepository.findByLoginIgnoreCase(login).orElse(null);
    }

    public void deletarFuncionario(Long funcionarioId) {
        List<FuncionarioProjeto> funcionarioProjetos = funcionarioProjetoRepository.findAll();

        if(funcionarioProjetos.isEmpty()){
            funcionarioRepository.deleteById(funcionarioId);
        } else throw new IllegalArgumentException("Funcionário atribuído a algum projeto");
    }

    public void updateFuncionario(Long id, String nome, EnumCargoFuncionario cargo, String login, String senha) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);

        if (funcionario.isPresent()) {
            if(!nome.isBlank())
                funcionario.get().setNome(nome);
            if(cargo != null)
                funcionario.get().setCargo(cargo);
            if(login != null)
                funcionario.get().setLogin(login);
            if(senha != null)
                funcionario.get().setSenha(passwordEncoder.encode(senha));

            funcionarioRepository.save(funcionario.get());
        } else throw new IllegalArgumentException("Funcionário não encontrado");
    }
}
