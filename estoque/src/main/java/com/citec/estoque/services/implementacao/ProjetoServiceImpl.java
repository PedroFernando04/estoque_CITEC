package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.enums.EnumStatusProjeto;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import com.citec.estoque.repositorys.tabelasAuxiliares.FuncionarioProjetoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.FuncionarioRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ProjetoRepository;
import com.citec.estoque.services.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoServiceImpl implements ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private FuncionarioProjetoRepository funcionarioProjetoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;


    public void salvarProjeto(Projeto projeto) {
        Optional<Projeto> projetoRetorno = projetoRepository.findByNomeIgnoreCase(projeto.getNome());

        if (projetoRetorno.isPresent())
            throw new IllegalArgumentException("Nome Duplicado");

        else
            projetoRepository.save(projeto);
    }

    public void atualizarProjeto(Long id, String nome, String solicitante, EnumStatusProjeto status, List<Long> funcionariosId) {
        Optional<Projeto> projeto= projetoRepository.findById(id);

        List<FuncionarioProjeto> funcionarioProjetoRetorno = funcionarioProjetoRepository.findByProjetoId(id);



        if (projeto.isPresent()) {
            if (!nome.isBlank())
                projeto.get().setNome(nome);
            if (!solicitante.isBlank())
                projeto.get().setNomeSolicitante(solicitante);
            if (status != null)
                projeto.get().setStatusProjeto(status);
            if (funcionariosId != null && !funcionariosId.isEmpty()){
                List<Funcionario> funcionariosRetorno = funcionarioRepository.findAllById(funcionariosId);

                for (FuncionarioProjeto  funcionario : funcionarioProjetoRetorno) {
                    funcionarioProjetoRepository.deleteById(funcionario.getId());
                }

                for (Funcionario funcionarioProjeto : funcionariosRetorno) {
                    FuncionarioProjeto funcionarioProjetolLoop = new FuncionarioProjeto(projeto.get(), funcionarioProjeto);

                    funcionarioProjetoRepository.save(funcionarioProjetolLoop);
                }
            }

            projetoRepository.save(projeto.get());

        }
    }
}
