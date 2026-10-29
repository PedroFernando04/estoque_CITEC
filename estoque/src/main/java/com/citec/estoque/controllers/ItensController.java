package com.citec.estoque.controllers;

import com.citec.estoque.dtos.ItensComQuantidadeDTO;
import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemEstoqueRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.services.EstoqueService;
import com.citec.estoque.services.ItemService;
import com.citec.estoque.specification.tabelasAuxiliares.ItemEstoqueSpecification;
import com.citec.estoque.specification.tabelasPrincipais.ItemSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping
public class ItensController {

    @Autowired
    private ItemEstoqueRepository itemEstoqueRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EstoqueService estoqueService;


    @GetMapping("/itens/projetos/{id}")
    public String itens(Model model,
                        @PathVariable Long id,
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "15") Integer size){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<ItemEstoque> spec =
                Specification.where(ItemEstoqueSpecification.comItem(id));

        Page<ItemEstoque> pagina = itemEstoqueRepository.findAll(spec, pageRequest);

        List<Estoque> estoques = pagina.stream()
                        .map(ItemEstoque::getEstoque)
                            .filter(Objects::nonNull)
                                .distinct()
                                    .toList();


        model.addAttribute("estoques", estoques);
        model.addAttribute("pagina", pagina);
        model.addAttribute("itemEstoque", pagina.getContent());
        model.addAttribute("item", itemService.buscarItemComQuantidade(id));

        return "itens/itensProjeto";
    }

    @GetMapping("/itens/cadastrar")
    public String cadastrarItem(){

        return "itens/cadastrarItem";
    }

    @PostMapping("/itens/cadastrar")
    public String cadastrarItem(@RequestParam(required = false) String nome,
                                @RequestParam(required = false) String rm,
                                @RequestParam String nomeFantasia,
                                @RequestParam Integer quantidade,
                                @RequestParam(required = false )MultipartFile foto) {
         try {
             String rmLimpo = itemService.limparRm(rm);

             Item item = new Item();

             item.setNome(nome);
             item.setNomeFantasia(nomeFantasia);
             item.setCodigoRM(rmLimpo);
             item.setImagePath(itemService.salvarFoto(foto));

             itemService.salvarItem(item);

             estoqueService.inserirItem(item.getNome(), quantidade, 1L, null);

         } catch (Exception e){
             throw new  IllegalArgumentException("Erro ao cadastrar Item:  " + e.getMessage());
         }

         return "redirect:/itens";
    }

    @GetMapping("/itens")
    public String itensHome(Model model,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "15") Integer size,
                            @RequestParam(required = false) String nome,
                            @RequestParam(required = false) Boolean faltando){

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Specification<Item> spec = Specification.where(ItemSpecification.buscaGlobal(nome))
                                    .and(ItemSpecification.comFaltando(faltando));

        Page<ItensComQuantidadeDTO> pagina = itemService.listarItensComQuantidade(spec, pageRequest);
        model.addAttribute("pagina", pagina);

        model.addAttribute("itensExistentes", pagina.getContent());

        return "itens/itensHome";
    }

    @PostMapping(value = "/itens", params = "remove")
    public String removerItem(@RequestParam Long remove){

        Optional<Item> itemExcluido = itemRepository.findById(remove);

        try {
            itemService.removerItem(itemExcluido.get());
        }  catch (Exception e) {
            throw new IllegalArgumentException("Não é possível remover um item presente em algum local:  " + e.getMessage());
        }


        return "redirect:/itens";
    }


    @PostMapping(value = "/itens", params = "faltando")
    public String localFaltando( Long faltando){

        try {
            itemService.atualizarItemFaltando(faltando);

        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao registrar falta no item: " +e.getMessage());
        }

        return "redirect:/itens";
    }

    @GetMapping("/itens/{id}")
    public String editarItem(Model model, @PathVariable Long id){

        ItensComQuantidadeDTO item = itemService.buscarItemComQuantidade(id);
        model.addAttribute("item", item);

        return "itens/editarItens";
    }

    @PostMapping(value = "/itens/{id}", params = "update")
    public String editarItem(@PathVariable Long id,
                             @RequestParam(required = false) String nomeFantasia,
                             @RequestParam(required = false) String nome,
                             @RequestParam(required = false) String rm,
                             @RequestParam(required = false) Integer quantidade,
                             @RequestParam(required = false) MultipartFile foto) {

        try {
            itemService.updateItem(id, nomeFantasia, nome, rm, quantidade, foto);
        } catch (Exception e){
            throw new IllegalArgumentException("Erro ao atualizar Item:  " + e.getMessage());
        }


        return "redirect:/itens";
    }
}
