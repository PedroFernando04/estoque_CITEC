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
import com.citec.estoque.specification.tabelasPrincipais.PedidoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Long destinoId,
                          @RequestParam(required = false) Long itemId,
                          @RequestParam(required = false) EnumStatusPedido status) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Specification<Pedido> spec =
                Specification.where(PedidoSpecification.comDestino(destinoId)
                .and(PedidoSpecification.comStatus(status)
                        .and(PedidoSpecification.comItem(itemId))));

        Page<PedidoComItensDTO> pagina = pedidoService.listarPedidosComItens(spec, pageRequest);
        model.addAttribute("pagina", pagina);
        model.addAttribute("pedidos", pagina.getContent());

        model.addAttribute("status", Arrays.asList(EnumStatusPedido.values()));

        List<Estoque> destinos = pagina.getContent().stream()
                .map(PedidoComItensDTO::getDestino)
                    .distinct()
                        .toList();
        model.addAttribute("destinos", destinos);

        Page<Item> itens = itemRepository.findAll(pageRequest);
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
        try{
            Pedido pedido = new Pedido();
            pedido.setDestino(destino);
            pedido.setDataPedido(LocalDateTime.now());
            pedido.setStatusPedido(status);

            pedidoRepository.save(pedido);

            Long id = pedido.getId();

            return "redirect:/pedidos/" + id;
        } catch(Exception e){
            throw new IllegalArgumentException("Erro ao cadastrar pedido: " +e.getMessage());
        }
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

        List<EnumStatusPedido> status = Arrays.asList(EnumStatusPedido.values());
        model.addAttribute("statusExistentes", status);

        return "pedidos/detalhesPedido";
    }

    //INSERT
    @PostMapping( value = "/pedidos/{id}", params = "add")
    public String detalhesPedidos(@PathVariable Long id, Model model,
                                  @RequestParam String itemNome,
                                  @RequestParam Integer quantidade){

        try {
            Optional<Item> item = itemRepository.findByNomeIgnoreCase(itemNome);
            Optional<Pedido> pedido = pedidoRepository.findById(id);

            pedidoService.salvarPedido(item, pedido, quantidade);
        } catch(Exception e){
            throw new IllegalArgumentException("Erro ao inserir item no pedido: " +e.getMessage());
        }

        return "redirect:/pedidos/{id}";
    }

    //DELETE
    @PostMapping(value = "/pedidos/{id}", params = "remove")
    public String removerPedidos(@PathVariable Long id, Model model, @RequestParam Long remove){

        try {
            pedidoService.deletarItem(remove);
        } catch(Exception e){
            throw new IllegalArgumentException("Erro ao deletar item no pedido: " +e.getMessage());
        }

        return "redirect:/pedidos/{id}";
    }
    
    //  UPDATE
    @PostMapping(value = "/pedidos/{id}", params = "update")
    public String atualizarPedidos(@PathVariable Long id, Model model, @RequestParam EnumStatusPedido novoStatus){

        List<EnumStatusPedido> status = Arrays.asList(EnumStatusPedido.values());
        model.addAttribute("statusExistentes", status);

        try {
            pedidoService.atualizarStatus(novoStatus, id);
        } catch(Exception e){
            throw new IllegalArgumentException("Erro ao atualizar pedido: " +e.getMessage());
        }

        return "redirect:/pedidos/{id}";
    }
}
