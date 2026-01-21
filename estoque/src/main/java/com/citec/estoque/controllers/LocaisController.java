package com.citec.estoque.controllers;

import com.citec.estoque.entities.enums.EnumStatusMovimentacao;
import com.citec.estoque.entities.enums.EnumStatusProjeto;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import com.citec.estoque.entities.tabelasAuxiliares.Movimentacao;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import com.citec.estoque.repositorys.tabelasAuxiliares.FuncionarioProjetoRepository;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemEstoqueRepository;
import com.citec.estoque.repositorys.tabelasAuxiliares.MovimentacaoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.EstoqueRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.FuncionarioRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ProjetoRepository;
import com.citec.estoque.services.EstoqueService;
import com.citec.estoque.services.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class LocaisController {
    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private ItemEstoqueRepository itemEstoqueRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private FuncionarioProjetoRepository funcionarioProjetoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping({"/", "/locais"})
    public String locais(Model model,
                         @RequestParam(defaultValue = "0") Integer page,
                         @RequestParam(defaultValue = "18") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Estoque> pagina = estoqueRepository.findAll(pageRequest);

        List<Estoque> estoques = pagina.getContent();
        List<Projeto> projetos = projetoRepository.findAll();

        List<String> solicitantesExistentes = projetos.stream()
                        .map(Projeto::getNomeSolicitante)
                        .distinct()
                        .filter(s -> s != null && !s.isEmpty())
                        .toList();


        List<EnumStatusProjeto> statusExistentes = Arrays.asList(EnumStatusProjeto.values());



        model.addAttribute("statusExistentes", statusExistentes);
        model.addAttribute("solicitantesExistentes", solicitantesExistentes);
        model.addAttribute("locais", estoques);
        model.addAttribute("pagina", pagina);



        return "estoquesProjetos/locais";
    }

    @GetMapping("/locais/cadastrar")
    public String cadastrarLocal(Model model) {

        List<EnumStatusProjeto> statusExistentes = Arrays.asList(EnumStatusProjeto.values());
        model.addAttribute("statusExistentes", statusExistentes);

        List<Funcionario> funcionariosExistentes = funcionarioRepository.findAll();
        model.addAttribute("funcionariosExistentes", funcionariosExistentes);

        return "estoquesProjetos/cadastrarLocal";
    }

    @PostMapping("/locais/cadastrar")
    public String cadastrarLocal(@RequestParam String tipo,
                                 @RequestParam String nome,
                                 @RequestParam(value = "solicitante", required = false) String nomeSolicitante,
                                 @RequestParam(value = "status", required = false) EnumStatusProjeto status,
                                 @RequestParam(value = "funcionario", required = false) List<Long> funcionariosIds,
                                 Model model) {

        model.addAttribute("tipo", tipo);

        try {
            if (tipo.equals("estoque")) {
                Estoque estoque = new Estoque();
                estoque.setNome(nome);

                estoqueService.salvarEstoque(estoque);
            }

            else if (tipo.equals("projeto")){
                Projeto projeto = new Projeto();
                projeto.setNome(nome);
                projeto.setNomeSolicitante(nomeSolicitante);
                projeto.setStatusProjeto(status);

                projetoService.salvarProjeto(projeto);


                for(Long funcionarioId : funcionariosIds){

                    FuncionarioProjeto funcionarioProjeto = new FuncionarioProjeto();
                    Funcionario funcionario = funcionarioRepository.findById(funcionarioId).get();
                    funcionarioProjeto.setFuncionario(funcionario);
                    funcionarioProjeto.setProjeto(projeto);

                    funcionarioProjetoRepository.save(funcionarioProjeto);
                }


            }
            else
                throw new IllegalArgumentException();

            return "redirect:/locais";

        } catch (IllegalArgumentException e) {
            return "redirect:/locais/cadastrar";
        }
    }

    @GetMapping("/local/{id}")
    public String local(Model model, @PathVariable Long id) {
        Optional<Estoque> estoque = estoqueRepository.findById(id);

        if (estoque.isPresent() && estoque.get().getTipo().equals("Projeto")) {
            model.addAttribute("local", projetoRepository.findById(id).get());

            List<FuncionarioProjeto> funcionariosLocal = funcionarioProjetoRepository.findByProjetoId(id);
            model.addAttribute("funcionariosLocal", funcionariosLocal);
        }
        else if (estoque.isPresent() && estoque.get().getTipo().equals("Estoque")) {
            model.addAttribute("local", estoqueRepository.findById(id).get());
        }
        else
            throw new IllegalArgumentException("Problema no tipo do local");


        List<ItemEstoque> itemEstoqueLocal = itemEstoqueRepository.findByEstoqueId(id);
        model.addAttribute("itensLocal", itemEstoqueLocal);

        List<Item> itensExistentes = itemRepository.findAll();
        model.addAttribute("itensExistentes", itensExistentes);

        List<Movimentacao> movimentacoesOrigem = movimentacaoRepository.findByOrigemId(id);
        List<Movimentacao> movimentacoesDestino = movimentacaoRepository.findByDestinoId(id);
        List<Movimentacao> movimentacaosLocal = new ArrayList<>();
        movimentacaosLocal.addAll(movimentacoesOrigem);
        movimentacaosLocal.addAll(movimentacoesDestino);
        model.addAttribute("movimentacoesLocal", movimentacaosLocal);


        return "estoquesProjetos/detalhesLocal";
    }

    @PostMapping("/local/{id}")
    public String local(@PathVariable Long id,
                        @RequestParam String itemNome,
                        @RequestParam Integer quantidade){

        estoqueService.inserirItem(itemNome, quantidade, id);

        return  "redirect:/local/{id}";
    }
}