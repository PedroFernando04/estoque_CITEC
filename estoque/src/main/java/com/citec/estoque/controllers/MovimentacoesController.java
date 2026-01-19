package com.citec.estoque.controllers;

import com.citec.estoque.entities.enums.EnumStatusMovimentacao;
import com.citec.estoque.entities.tabelasAuxiliares.Movimentacao;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.repositorys.tabelasAuxiliares.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping
public class MovimentacoesController {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @GetMapping("/historico")
    public String historico(Model model,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "15")  Integer size) {


        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Movimentacao> pagina = movimentacaoRepository.findAll(pageRequest);
        model.addAttribute("pagina", pagina);

        List<Movimentacao> movimentacoes = pagina.getContent();
        model.addAttribute("movimentacoes", movimentacoes);

        List<EnumStatusMovimentacao>  statusMovimentacoes = Arrays.asList(EnumStatusMovimentacao.values());
        model.addAttribute("statusExistentes", statusMovimentacoes);

        List<Estoque> origensExistentes = movimentacoes.stream()
                .map(Movimentacao::getOrigem)
                .distinct()
                .toList();
        model.addAttribute("origensExistentes", origensExistentes);

        List<Estoque> destinosExistentes = movimentacoes.stream()
                .map(Movimentacao::getDestino)
                .distinct()
                .toList();
        model.addAttribute("destinosExistentes", destinosExistentes);

        List<Item> itensExistentes = movimentacoes.stream()
                .map(Movimentacao::getItem)
                .distinct()
                .toList();
        model.addAttribute("itensExistentes", itensExistentes);

        return "historico/historicoHome";
    }
}
