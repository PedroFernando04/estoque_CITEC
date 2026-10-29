package com.citec.estoque.controllers;

import com.citec.estoque.entities.enums.EnumCargoFuncionario;
import com.citec.estoque.entities.enums.EnumStatusProjeto;
import com.citec.estoque.entities.tabelasAuxiliares.FuncionarioProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;
import com.citec.estoque.repositorys.tabelasAuxiliares.FuncionarioProjetoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.FuncionarioRepository;
import com.citec.estoque.services.FuncionarioService;
import com.citec.estoque.specification.tabelasAuxiliares.FuncionarioProjetoSpecification;
import com.citec.estoque.specification.tabelasPrincipais.FuncionarioSpecification;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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

    @GetMapping("/funcionarios/projetos/{id}")
    public String funcionarios(Model model,
                               @PathVariable Long id,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "15") Integer size,
                               @RequestParam(required = false) Long projeto,
                               @RequestParam(required = false) EnumStatusProjeto status) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<FuncionarioProjeto> spec =
            Specification.where(FuncionarioProjetoSpecification.comFuncionarioId(id)
                .and(FuncionarioProjetoSpecification.comProjeto(projeto)))
                    .and(FuncionarioProjetoSpecification.comStatus(status));

        Page<FuncionarioProjeto> pagina = funcionarioProjetoRepository.findAll(spec, pageRequest);
        model.addAttribute("pagina", pagina);
        model.addAttribute("funcionarioProjetos", pagina.getContent());
        model.addAttribute("funcionario", funcionarioRepository.findById(id).get());

        List<Projeto> projetosExistentes = pagina.getContent().stream()
                .map(FuncionarioProjeto::getProjeto)
                    .filter(Objects::nonNull)
                        .distinct()
                            .toList();
        model.addAttribute("projetosExistentes", projetosExistentes);

        List<EnumStatusProjeto> statusProjetosExistentes = Arrays.asList(EnumStatusProjeto.values());
        model.addAttribute("statusProjetosExistentes", statusProjetosExistentes);

        return "funcionarios/funcionariosProjetos";
    }

    @GetMapping("/funcionarios/cadastrar")
    public String cadastrarFuncionario(Model model) {

        List<EnumCargoFuncionario> cargosExistentes = Arrays.asList(EnumCargoFuncionario.values());
        model.addAttribute("cargosExistentes", cargosExistentes);


        return "funcionarios/cadastrarFuncionario";
    }

    @PostMapping("/funcionarios/cadastrar")
    public String cadastrarFuncionario(@RequestParam String nome,
                                       @RequestParam EnumCargoFuncionario cargo,
                                       @RequestParam String login,
                                       @RequestParam String senha) {

        try {
            Funcionario funcionario = new Funcionario();
            funcionario.setNome(nome);
            funcionario.setCargo(cargo);
            funcionario.setLogin(login);

            funcionarioService.salvarFuncionario(funcionario, senha);

        } catch (Exception e) {
            throw new  RuntimeException("Erro ao cadastrar funcionario:  " + e.getMessage());
        }

        return "redirect:/funcionarios";
    }

    @GetMapping("/funcionarios")
    public String funcionariosCadastrados(Model model,
                                          @RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "3") Integer size,
                                          @RequestParam(required = false) EnumCargoFuncionario cargo,
                                          @RequestParam(required = false) String nome) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<Funcionario> spec =
                Specification.where(FuncionarioSpecification.comNome(nome))
                        .and(FuncionarioSpecification.comCargo(cargo));

        Page<Funcionario> pagina = funcionarioRepository.findAll(spec, pageRequest);
        model.addAttribute("pagina", pagina);

        model.addAttribute("funcionariosExistentes", pagina.getContent());

        List<EnumCargoFuncionario> cargosExistentes = Arrays.asList(EnumCargoFuncionario.values());
        model.addAttribute("cargosExistentes", cargosExistentes);

        return "funcionarios/funcionariosHome";
    }

    @PostMapping(value = "/funcionarios", params = "remove")
    public String removerFuncionario(Model model, @RequestParam Long remove) {

        try{
            funcionarioRepository.deleteById(remove);
        } catch(Exception e) {
            throw new IllegalArgumentException("Não é possível excluir um funcionário associado a algum projeto");
        }

        return  "redirect:/funcionarios";
    }

    @GetMapping("/funcionarios/{id}")
    public String updateFuncionario(@PathVariable Long id, Model model) {

        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        model.addAttribute("funcionario", funcionario.get());

        List<EnumCargoFuncionario> cargosExistentes = Arrays.asList(EnumCargoFuncionario.values());
        model.addAttribute("cargosExistentes", cargosExistentes);

        return  "/funcionarios/editarFuncionario";
    }

    @PostMapping(value = "/funcionarios/{id}", params = "update")
    public String updateFuncionario(@PathVariable Long id,
                                    @RequestParam String nome,
                                    @RequestParam EnumCargoFuncionario clero,
                                    @RequestParam String login,
                                    @RequestParam String senha) {

        try {
            funcionarioService.updateFuncionario(id, nome, clero, login, senha);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar Funcionário:  " + e.getMessage());
        }

        return "redirect:/funcionarios";
    }

    @GetMapping("/entrar")
    public String login() { return "login"; }

    @PostMapping("/entrar")
    public String entrar(@RequestParam String login,
                         @RequestParam String senha,
                         SecurityContext currentContext,
                         HttpServletRequest request,
                         HttpServletResponse response) {

        Funcionario funcionario = funcionarioService.getFuncionarioByLogin(login);

        if (funcionario == null || !funcionarioService.verifyPassword(senha, funcionario.getSenha())) {
           return "redirect:/login?error=true";
        }

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(funcionario, null, authorities);

        currentContext.setAuthentication(authentication);

        SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.saveContext(currentContext, request, response);

        return "redirect:/";
    }
}
