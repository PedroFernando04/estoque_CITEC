package com.citec.estoque.controllers;

import com.citec.estoque.dtos.pedidos.PedidoComItensDTO;
import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemPedidoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.PedidoRepository;
import com.citec.estoque.services.PedidoService;
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
public class PedidosController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PedidoService pedidoService;


    @GetMapping("/pedidos")
    public String pedidos(Model model,
                          @RequestParam(defaultValue = "0") Integer page,
                          @RequestParam(defaultValue = "1") Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Pedido> pagina = pedidoRepository.findAll(pageRequest);
        model.addAttribute("pagina", pagina);

        List<PedidoComItensDTO> pedidos = pedidoService.listarPedidosComItens();
        model.addAttribute("pedidos", pedidos);

        List<EnumStatusPedido> status = Arrays.asList(EnumStatusPedido.values());
        model.addAttribute("status", status);

        List<Estoque> destinos = pedidos.stream()
                .map(PedidoComItensDTO::getDestino)
                    .distinct()
                        .toList();
        model.addAttribute("destinos", destinos);

        List<Item> itens = itemRepository.findAll();
        model.addAttribute("itens", itens);

        return "pedidos/pedidosHome";
    }

    @GetMapping("/pedidos/cadastrar")
    public String cadastrarPedidos(Model model){

        return "pedidos/cadastrarPedidos";
    }
}
