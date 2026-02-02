package com.citec.estoque.controllers;

import com.citec.estoque.entities.enums.EnumCategoriasItem;
import com.citec.estoque.entities.enums.EnumCleroFuncionario;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import com.citec.estoque.repositorys.tabelasAuxiliares.FuncionarioProjetoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.FuncionarioRepository;
import com.citec.estoque.services.FuncionarioService;
import com.citec.estoque.specification.tabelasAuxiliares.FuncionarioProjetoSpecification;
import com.citec.estoque.specification.tabelasPrincipais.FuncionarioSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping
public class FuncionariosController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private FuncionarioProjetoRepository funcionarioProjetoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping("/funcionarios")
    public String funcionarios(Model model,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "15") Integer size,
                               @RequestParam(required = false) String nome,
                               @RequestParam(required = false) EnumCleroFuncionario clero,
                               @RequestParam(required = false) Long projeto) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<FuncionarioProjeto> spec =
            Specification.where(FuncionarioProjetoSpecification.comNome(nome))
                    .and(FuncionarioProjetoSpecification.comClero(clero))
                    .and(FuncionarioProjetoSpecification.comProjeto(projeto));

        Page<FuncionarioProjeto> pagina = funcionarioProjetoRepository.findAll(spec, pageRequest);
        model.addAttribute("pagina", pagina);
        model.addAttribute("funcionarioProjetos", pagina.getContent());

        List<Projeto> projetosExistentes = pagina.getContent().stream()
                .map(FuncionarioProjeto::getProjeto)
                    .filter(Objects::nonNull)
                        .distinct()
                            .toList();
        model.addAttribute("projetosExistentes", projetosExistentes);

        List<EnumCleroFuncionario> clerosExistentes = Arrays.asList(EnumCleroFuncionario.values());
        model.addAttribute("clerosExistentes", clerosExistentes);

        return "funcionarios/funcionariosHome";
    }

    @GetMapping("/funcionarios/cadastrar")
    public String cadastrarFuncionario(Model model) {

        List<EnumCleroFuncionario> clerosExistentes = Arrays.asList(EnumCleroFuncionario.values());
        model.addAttribute("clerosExistentes", clerosExistentes);


        return "funcionarios/cadastrarFuncionario";
    }

    @PostMapping("/funcionarios/cadastrar")
    public String cadastrarFuncionario(@RequestParam String nome,
                                       @RequestParam EnumCleroFuncionario clero) {

        try {
            Funcionario funcionario = new Funcionario();
            funcionario.setNome(nome);
            funcionario.setClero(clero);

            funcionarioService.salvarFuncionario(funcionario);

        } catch (Exception e) {
            throw new  RuntimeException("Erro ao cadastrar funcionario:  " + e.getMessage());
        }

        return "redirect:/funcionarios";
    }

    @GetMapping("/funcionarios/cadastrados")
    public String funcionariosCadastrados(Model model,
                                          @RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "3") Integer size,
                                          @RequestParam(required = false) EnumCleroFuncionario clero,
                                          @RequestParam(required = false) String nome) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<Funcionario> spec =
                Specification.where(FuncionarioSpecification.comNome(nome))
                        .and(FuncionarioSpecification.comClero(clero));

        Page<Funcionario> pagina = funcionarioRepository.findAll(spec, pageRequest);
        model.addAttribute("pagina", pagina);

        model.addAttribute("funcionariosExistentes", pagina.getContent());

        List<EnumCleroFuncionario> clerosExistentes = Arrays.asList(EnumCleroFuncionario.values());
        model.addAttribute("clerosExistentes", clerosExistentes);

        return "funcionarios/funcionariosCadastrados";
    }

    @PostMapping(value = "/funcionarios/cadastrados", params = "remove")
    public String removerFuncionario(Model model, @RequestParam Long remove) {

        try{
            funcionarioRepository.deleteById(remove);
        } catch(Exception e) {
            throw new IllegalArgumentException("Não é possível excluir um funcionário associado a algum projeto");
        }

        return  "redirect:/funcionarios/cadastrados";
    }

    @GetMapping("/funcionarios/{id}")
    public String updateFuncionario(@PathVariable Long id, Model model) {

        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        model.addAttribute("funcionario", funcionario.get());

        List<EnumCleroFuncionario> clerosExistentes = Arrays.asList(EnumCleroFuncionario.values());
        model.addAttribute("clerosExistentes", clerosExistentes);

        return  "/funcionarios/editarFuncionario";
    }

    @PostMapping(value = "/funcionarios/{id}", params = "update")
    public String updateFuncionario(@PathVariable Long id, @RequestParam String nome, @RequestParam EnumCleroFuncionario clero) {

        try {
            funcionarioService.updateFuncionario(id, nome, clero);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar Funcionário:  " + e.getMessage());
        }

        return "redirect:/funcionarios/cadastrados";
    }
}
