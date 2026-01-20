package com.citec.estoque.controllers;

import com.citec.estoque.dtos.pedidos.PedidoComItensDTO;
import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemPedidoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.EstoqueRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.PedidoRepository;
import com.citec.estoque.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("pedidoForm")
@RequestMapping
public class PedidosController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;


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

        List<Item> itens = itemRepository.findAll();
        model.addAttribute("itens", itens);

        List<EnumStatusPedido> status = Arrays.asList(EnumStatusPedido.values());
        model.addAttribute("statusExistentes", status);

        List<Estoque> destinos = estoqueRepository.findAll();
        model.addAttribute("destinos", destinos);

        return "pedidos/cadastrarPedidos";
    }

    @PostMapping("/pedidos/cadastrar")
    public String cadastrarPedidos(@RequestParam Estoque destino,
                                   @RequestParam EnumStatusPedido status) {
        Pedido pedido = new Pedido();
        pedido.setDestino(destino);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatusPedido(status);

        pedidoRepository.save(pedido);

        Long id = pedido.getId();

        return "redirect:/pedidos/" + id;
    }

    @GetMapping("/pedidos/{id}")
    public String detalhesPedido(@PathVariable Long id, Model model){

        Optional<Pedido> pedido = pedidoRepository.findById(id);

        List<Item> itensExistentes = itemRepository.findAll();
        model.addAttribute("itensExistentes", itensExistentes);

        if (pedido.isPresent()) {
            model.addAttribute("pedido", pedido.get());

            List<ItemPedido> itensPedido = itemPedidoRepository.findByPedido(pedido.get());
            model.addAttribute("itensLocal", itensPedido);
        }
        else throw new IllegalArgumentException("Pedido não encontrado");



        return "pedidos/detalhesPedido";
    }

    @PostMapping("/pedidos/{id}")
    public String detalhesPedidos(@PathVariable Long id, Model model,
                                  @RequestParam String itemNome,
                                  @RequestParam Integer quantidade){

        Optional<Item> item = itemRepository.findByNomeIgnoreCase(itemNome);
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (item.isPresent() && pedido.isPresent()) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(quantidade);
            itemPedido.setItem(item.get());
            itemPedido.setPedido(pedido.get());

            itemPedidoRepository.save(itemPedido);
        } else throw new IllegalArgumentException("Item não encontrado");


        return "redirect:/pedidos/{id}";
    }
}
