package com.citec.estoque.controllers;

import com.citec.estoque.entities.enums.EnumCleroFuncionario;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import com.citec.estoque.repositorys.tabelasAuxiliares.FuncionarioProjetoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.FuncionarioRepository;
import com.citec.estoque.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
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
                               @RequestParam(defaultValue = "15") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<FuncionarioProjeto> pagina = funcionarioProjetoRepository.findAll(pageRequest);
        model.addAttribute("pagina", pagina);

        List<FuncionarioProjeto> funcionarioProjetos =  pagina.getContent();
        model.addAttribute("funcionarioProjetos", funcionarioProjetos);

        List<Projeto> projetosExistentes = funcionarioProjetos.stream()
                .map(FuncionarioProjeto::getProjeto)
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
            return "redirect:/funcionarios/cadastrar";
        }

        return "redirect:/funcionarios";
    }

    @GetMapping("/funcionarios/cadastrados")
    public String funcionariosCadastrados(Model model,
                                          @RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "15") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Funcionario> pagina = funcionarioRepository.findAll(pageRequest);
        model.addAttribute("pagina", pagina);

        List<Funcionario> funcionariosExistentes = pagina.getContent();
        model.addAttribute("funcionariosExistentes", funcionariosExistentes);

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
        } catch (Exception e) { return "redirect:/funcionarios/{" + id + "}"; }

        return "redirect:/funcionarios/cadastrados";
    }
}
