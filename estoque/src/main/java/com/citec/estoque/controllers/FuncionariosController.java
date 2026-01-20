package com.citec.estoque.controllers;

import com.citec.estoque.entities.enums.EnumCleroFuncionario;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import com.citec.estoque.repositorys.tabelasAuxiliares.FuncionarioProjetoRepository;
import com.citec.estoque.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping
public class FuncionariosController {

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private FuncionarioProjetoRepository funcionarioProjetoRepository;


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

        } catch (IllegalArgumentException e) {
            return "redirect:/funcionarios/cadastrar";
        }

        return "redirect:/funcionarios";
    }
}
