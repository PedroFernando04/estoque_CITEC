package com.citec.estoque.controllers;

import com.citec.estoque.dtos.PedidoComItensDTO;
import com.citec.estoque.entities.enums.EnumCategoriaPedido;
import com.citec.estoque.entities.enums.EnumStatusPedido;
import com.citec.estoque.entities.enums.tipoPedido.EnumTipoOsPedido;
import com.citec.estoque.entities.enums.tipoPedido.EnumTipoTransportePedido;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemPedidoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.EstoqueRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.PedidoRepository;
import com.citec.estoque.services.ItemService;
import com.citec.estoque.services.PedidoService;
import com.citec.estoque.specification.tabelasPrincipais.PedidoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ItemService itemService;


    @GetMapping("/pedidos")
    public String pedidos(Model model,
                          @RequestParam(defaultValue = "0") Integer page,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) Long destinoId,
                          @RequestParam(required = false) Long itemId,
                          @RequestParam(required = false) EnumStatusPedido status) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

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

    @PostMapping(value = "/pedidos", params = "remove")
    public String remove(@RequestParam Long remove){
        try {
            pedidoRepository.deleteById(remove);
            } catch (Exception e){ throw new IllegalArgumentException("Erro ao tentar remover o pedido: " + e.getMessage()); }

        return "redirect:/pedidos";
    }

    @PostMapping(value = "/pedidos", params = "update")
    public String updateStatus(@RequestParam Long update,
                               @RequestParam EnumStatusPedido novoStatus){

        try{
            pedidoService.atualizarStatus(novoStatus, update);
        } catch (Exception e){throw new  IllegalArgumentException("Erro ao tentar atualizar status: " + e.getMessage()); }

        return "redirect:/pedidos";
    }

    @GetMapping("/pedidos/cadastrar")
    public String cadastrarPedidos(Model model){

        List<EnumStatusPedido> status = Arrays.asList(EnumStatusPedido.values());
        model.addAttribute("statusExistentes", status);

        List<Estoque> destinos = estoqueRepository.findAll();
        model.addAttribute("destinos", destinos);

        List<EnumCategoriaPedido>  categoriasExistentes = Arrays.asList(EnumCategoriaPedido.values());
        model.addAttribute("categoriasExistentes", categoriasExistentes);

        List<EnumTipoOsPedido>  tiposOsExistentes = Arrays.asList(EnumTipoOsPedido.values());
        model.addAttribute("tiposOsExistentes", tiposOsExistentes);

        List<EnumTipoTransportePedido>  tiposTransporteExistentes = Arrays.asList(EnumTipoTransportePedido.values());
        model.addAttribute("tiposTransporteExistentes", tiposTransporteExistentes);

        return "pedidos/cadastrarPedidos";
    }

    @PostMapping("/pedidos/cadastrar")
    public String cadastrarPedidos(@RequestParam Estoque destino,
                                   @RequestParam EnumStatusPedido status,
                                   @RequestParam String titulo,
                                   @RequestParam(required = false) String descricao,
                                   @RequestParam EnumCategoriaPedido categoria,
                                   @RequestParam(required = false) EnumTipoOsPedido tipoOS,
                                   @RequestParam(required = false) EnumTipoTransportePedido tipoTransporte) {
        try{
            Pedido pedido = new Pedido();
            pedido.setDestino(destino);
            pedido.setDataPedido(LocalDateTime.now());
            pedido.setStatusPedido(status);
            pedido.setTitulo(titulo);
            pedido.setDescricao(descricao);
            pedido.setCategoria(categoria);
            pedido.setTipoOs(tipoOS);
            pedido.setTipoTransporte(tipoTransporte);

            pedidoRepository.save(pedido);

            Long id = pedido.getId();

            return "redirect:/pedidos/" + id;

        } catch(Exception e){
            throw new IllegalArgumentException("Erro ao cadastrar pedido: " + e.getMessage());
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
    public String removerPedidos(@PathVariable Long id, @RequestParam Long remove){

        try {
            pedidoService.deletarItem(remove);
        } catch(Exception e){
            throw new IllegalArgumentException("Erro ao deletar item no pedido: " + e.getMessage());
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

    @PostMapping(value = "/pedidos/{id}", params = "more")
    public String adicionarUmItem(@PathVariable Long id, @RequestParam Long more){
        try{
            itemService.adicionarUmItemPedido(more);
        } catch(Exception e){throw new IllegalArgumentException("Erro ao inserir item no pedido: " + e.getMessage());}

        return "redirect:/pedidos/{id}";
    }

    @PostMapping(value = "/pedidos/{id}", params = "less")
    public String removerUmItem(@PathVariable Long id, @RequestParam Long less){
        try{
            itemService.removerUmItemPedido(less);
        } catch (Exception e){throw new IllegalArgumentException("Erro ao inserir item no pedido: " + e.getMessage());}

        return "redirect:/pedidos/{id}";
    }

    @GetMapping("pedidos/{id}/editar")
    public String editarPedido(@PathVariable Long id, Model model){
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        model.addAttribute("pedido", pedido.get());

        List<EnumStatusPedido> status = Arrays.asList(EnumStatusPedido.values());
        model.addAttribute("statusExistentes", status);

        List<Estoque> destinos = estoqueRepository.findAll();
        model.addAttribute("destinos", destinos);

        List<EnumCategoriaPedido>  categoriasExistentes = Arrays.asList(EnumCategoriaPedido.values());
        model.addAttribute("categoriasExistentes", categoriasExistentes);

        List<EnumTipoOsPedido>  tiposOsExistentes = Arrays.asList(EnumTipoOsPedido.values());
        model.addAttribute("tiposOsExistentes", tiposOsExistentes);

        List<EnumTipoTransportePedido>  tiposTransporteExistentes = Arrays.asList(EnumTipoTransportePedido.values());
        model.addAttribute("tiposTransporteExistentes", tiposTransporteExistentes);

        return "pedidos/editarPedido";
    }

    @PostMapping("pedidos/{id}/editar")
    public String editarPedidos(@PathVariable Long id,
                                @RequestParam(required = false) Estoque destino,
                                @RequestParam(required = false) EnumStatusPedido statusPedido,
                                @RequestParam(required = false) String titulo,
                                @RequestParam(required = false) String descricao,
                                @RequestParam(required = false) EnumCategoriaPedido categoria,
                                @RequestParam(required = false) EnumTipoOsPedido tipoOs,
                                @RequestParam(required = false) EnumTipoTransportePedido tipoTransporte){

        try{
            pedidoService.atualizarPedido(id, destino, statusPedido, titulo, descricao, categoria, tipoOs, tipoTransporte);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao atualizar pedido: " + e.getMessage());
        }

        return "redirect:/pedidos/{id}";
    }
}
