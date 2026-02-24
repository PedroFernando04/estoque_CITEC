package com.citec.estoque.controllers;

import com.citec.estoque.entities.enums.EnumCategoriaProjeto;
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
import com.citec.estoque.specification.tabelasPrincipais.ProjetoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

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
                         @RequestParam(defaultValue = "12") Integer size,
                         @RequestParam(required = false) String nome,
                         @RequestParam(required = false) String solicitante,
                         @RequestParam(required = false) EnumStatusProjeto status,
                         @RequestParam(required = false) EnumCategoriaProjeto categoria) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<Projeto> spec =
                Specification.where(ProjetoSpecification.comNome(nome)
                        .and(ProjetoSpecification.comSolicitante(solicitante))
                        .and(ProjetoSpecification.comStatus(status))
                        .and(ProjetoSpecification.comCategoria(categoria)));

        Page<Projeto> pagina = projetoRepository.findAll(spec, pageRequest);

        List<Projeto> projetos = pagina.getContent();

        List<String> solicitantesExistentes = projetos.stream()
                        .map(Projeto::getNomeSolicitante)
                        .distinct()
                        .filter(s -> s != null && !s.isEmpty())
                        .toList();


        List<EnumStatusProjeto> statusExistentes = Arrays.asList(EnumStatusProjeto.values());
        List<EnumCategoriaProjeto>  categoriaExistentes = Arrays.asList(EnumCategoriaProjeto.values());

        model.addAttribute("statusExistentes", statusExistentes);
        model.addAttribute("categoriasExistentes", categoriaExistentes);
        model.addAttribute("solicitantesExistentes", solicitantesExistentes);
        model.addAttribute("locais", projetos);
        model.addAttribute("pagina", pagina);

        return "estoquesProjetos/locais";
    }

    @GetMapping("/locais/cadastrar")
    public String cadastrarLocal(Model model) {

        List<EnumStatusProjeto> statusExistentes = Arrays.asList(EnumStatusProjeto.values());
        model.addAttribute("statusExistentes", statusExistentes);

        List<Funcionario> funcionariosExistentes = funcionarioRepository.findAll();
        model.addAttribute("funcionariosExistentes", funcionariosExistentes);


        List<EnumCategoriaProjeto>  categoriaExistentes = Arrays.asList(EnumCategoriaProjeto.values());
        model.addAttribute("categoriasExistentes", categoriaExistentes);

        return "estoquesProjetos/cadastrarLocal";
    }

    @PostMapping("/locais/cadastrar")
    public String cadastrarLocal(
                                 @RequestParam String nome,
                                 @RequestParam(value = "solicitante") String nomeSolicitante,
                                 @RequestParam(value = "status") EnumStatusProjeto status,
                                 @RequestParam(value = "funcionario") List<Long> funcionariosIds,
                                 @RequestParam(value = "descricao", required = false) String descricaoProjeto,
                                 @RequestParam(value = "categoria") EnumCategoriaProjeto categoriaProjeto) {

        try {
                Projeto projeto = new Projeto();
                projeto.setNome(nome);
                projeto.setNomeSolicitante(nomeSolicitante);
                projeto.setStatusProjeto(status);
                projeto.setDescricao(descricaoProjeto);
                projeto.setCategoria(categoriaProjeto);

                projetoService.salvarProjeto(projeto, funcionariosIds);

                return "redirect:/local/" + projeto.getId();

            } catch (Exception e) {
                throw new IllegalArgumentException("Erro ao cadastrar Local:  " + e.getMessage());
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

        List<Movimentacao> movimentacaosLocal = movimentacaoRepository.findTop15ByOrigemOrDestino(id, PageRequest.of(0, 15));
        model.addAttribute("movimentacoesLocal", movimentacaosLocal);

        List<Estoque> locais = estoqueRepository.findAll();
        model.addAttribute("locaisExistentes", locais);

        return "estoquesProjetos/detalhesLocal";
    }

    @PostMapping(value = "/local/{id}", params = "botaoEnviar")
    public String local(@PathVariable Long id,
                        @RequestParam String itemNome,
                        @RequestParam Integer quantidade,
                        @RequestParam(required = false, defaultValue = "CITEC") String itemOrigem){
        try {
            estoqueService.inserirItem(itemNome, quantidade, id, itemOrigem);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao inserir item no Local:  " + e.getMessage());
        }

        return  "redirect:/local/{id}";
    }



    @PostMapping(value = "/local/{id}", params = "remove")
    public String removerItemLocal(Long remove){

        Optional<ItemEstoque> itemEstoque = itemEstoqueRepository.findById(remove);

        try {
            estoqueService.deletarItemEstoque(remove, itemEstoque.get().getQuantidade());

            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setData(LocalDateTime.now());
            movimentacao.setItem(itemEstoque.get().getItem());
            movimentacao.setQuantidade(itemEstoque.get().getQuantidade());
            movimentacao.setOrigem(itemEstoque.get().getEstoque());
            movimentacao.setStatus(EnumStatusMovimentacao.SAIDA);
            movimentacaoRepository.save(movimentacao);

        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao deletar item do local: " +e.getMessage());
        }

        return "redirect:/local/{id}";
    }

    @PostMapping(value = "/local/{id}", params = "estoque")
    public String devolverAoEstoque(@PathVariable Long id){



        return "redirect:/local/{id}";
    }

    @GetMapping("/local/{id}/editar")
    public String editar(Model model, @PathVariable Long id){

        Optional<Estoque> local = estoqueRepository.findById(id);
        model.addAttribute("local", local.get());

        List<EnumStatusProjeto> statusExistentes = Arrays.asList(EnumStatusProjeto.values());
        model.addAttribute("statusExistentes", statusExistentes);

        List<Funcionario> funcionariosExistentes = funcionarioRepository.findAll();
        model.addAttribute("funcionariosExistentes", funcionariosExistentes);

        List<EnumCategoriaProjeto>  categoriasExistentes = Arrays.asList(EnumCategoriaProjeto.values());
        model.addAttribute("categoriasExistentes", categoriasExistentes);

        return "estoquesProjetos/editarLocal";
    }

    @PostMapping(value = "/local/{id}/editar", params = "update")
    public String editar(Model model,
                         @PathVariable Long id,
                         @RequestParam(required = false) String nome,
                         @RequestParam(required = false) String solicitante,
                         @RequestParam(required = false) EnumStatusProjeto status,
                         @RequestParam(required = false) List<Long> funcionario,
                         @RequestParam(required = false) EnumCategoriaProjeto categoria,
                         @RequestParam(required = false) String descricao){

        try {
            Optional<Estoque> local = estoqueRepository.findById(id);


            if (local.get().getTipo().equals("Estoque")) {
                estoqueService.atualizarEstoque(id, nome);

            }
            if (local.get().getTipo().equals("Projeto")) {
                projetoService.atualizarProjeto(id, nome, solicitante, status, funcionario, descricao, categoria);
            }

            estoqueRepository.save(local.get());
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar local: " +e.getMessage());
        }


        return  "redirect:/local/{id}";
    }
}