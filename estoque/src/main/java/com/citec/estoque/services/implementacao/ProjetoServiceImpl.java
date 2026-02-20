package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.enums.EnumCategoriaProjeto;
import com.citec.estoque.entities.enums.EnumStatusProjeto;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import com.citec.estoque.repositorys.tabelasAuxiliares.FuncionarioProjetoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.EstoqueRepository;
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
    private EstoqueRepository estoqueRepository;

    @Autowired
    private FuncionarioProjetoRepository funcionarioProjetoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;


    public void salvarProjeto(Projeto projeto, List<Long> funcionariosIds) {
        Optional<Estoque> projetoRetorno = estoqueRepository.findByNomeIgnoreCase(projeto.getNome());

        if (projetoRetorno.isPresent())
            throw new IllegalArgumentException("Nome Duplicado");

        else if (funcionariosIds.isEmpty()) {
            throw new IllegalArgumentException("Nenhum funcionário definido");
        } else
            projetoRepository.save(projeto);

        for(Long funcionarioId : funcionariosIds){

            FuncionarioProjeto funcionarioProjeto = new FuncionarioProjeto();
            Funcionario funcionario = funcionarioRepository.findById(funcionarioId).get();
            funcionarioProjeto.setFuncionario(funcionario);
            funcionarioProjeto.setProjeto(projeto);

            funcionarioProjetoRepository.save(funcionarioProjeto);
        }
    }

    public void atualizarProjeto(Long id, String nome, String solicitante, EnumStatusProjeto status, List<Long> funcionariosId, String descricao, EnumCategoriaProjeto categoria) {
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
            if (!descricao.isBlank())
                projeto.get().setDescricao(descricao);
            if (categoria != null)
                projeto.get().setCategoria(categoria);

            projetoRepository.save(projeto.get());

        }
    }
}
