package com.citec.estoque.controllers;

import com.citec.estoque.entities.enums.EnumCategoriasItem;
import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemEstoqueRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
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


    @GetMapping("/itens")
    public String itens(Model model,
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "15") Integer size){

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ItemEstoque> pagina = itemEstoqueRepository.findAll(pageRequest);

        List<Estoque> estoques = pagina.stream()
                        .map(ItemEstoque::getEstoque)
                                .distinct()
                                        .toList();


        model.addAttribute("estoques", estoques);
        model.addAttribute("pagina", pagina);
        model.addAttribute("itemEstoque", pagina.getContent());
        model.addAttribute("categorias", Arrays.asList(EnumCategoriasItem.values()));

        return "itens/itensHome";
    }

    @GetMapping("/itens/cadastrar")
    public String cadastrarItem(Model model){

        List<EnumCategoriasItem> categoriasExistentes = Arrays.asList(EnumCategoriasItem.values());
        model.addAttribute("categoriasExistentes", categoriasExistentes);

        return "itens/cadastrarItem";
    }

    @PostMapping("/itens/cadastrar")
    public String cadastrarItem(@RequestParam String nome,
                                @RequestParam EnumCategoriasItem categoria,
                                @RequestParam String rm,
                                @RequestParam String patrimonio){
         try {
             Item item = new Item();

             item.setNome(nome);
             item.setCategoriaItem(categoria);
             item.setCodigoRM(rm);
             item.setCodigoPatrimonio(patrimonio);

             itemService.salvarItem(item);

         } catch (IllegalArgumentException e){
             return "redirect:/itens/cadastrar";
         }

         return "redirect:/itens";
    }

    @GetMapping("/itens/cadastrados")
    public String itensCadastrados(Model model,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "15") Integer size){

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> pagina = itemRepository.findAll(pageRequest);
        model.addAttribute("pagina", pagina);

        List<EnumCategoriasItem> categoriasExistentes = Arrays.asList(EnumCategoriasItem.values());
        model.addAttribute("categorias", categoriasExistentes);

        List<Item> itensExistentes = itemRepository.findAll();
        model.addAttribute("itensExistentes", itensExistentes);

        return "itens/itensCadastrados";
    }

    @PostMapping(value = "/itens/cadastrados", params = "remove")
    public String removerItem(@RequestParam Long remove){

        Optional<Item> itemExcluido = itemRepository.findById(remove);

        try {
            itemService.removerItem(itemExcluido.get());
        }  catch (IllegalArgumentException e) {
            return "redirect:/itens/cadastrados";
        }


        return "redirect:/itens/cadastrados";
    }

    @GetMapping("/itens/{id}")
    public String editarItem(Model model, @PathVariable Long id){

        Optional<Item> item = itemRepository.findById(id);
        model.addAttribute("item", item.get());

        List<EnumCategoriasItem> categoriasExistentes = Arrays.asList(EnumCategoriasItem.values());
        model.addAttribute("categoriasExistentes", categoriasExistentes);

        return "itens/editarItens";
    }

    @PostMapping(value = "/itens/{id}", params = "update")
    public String editarItem(@PathVariable Long id,
                             @RequestParam(required = false) String nome,
                             @RequestParam(required = false) EnumCategoriasItem categoria,
                             @RequestParam(required = false) String rm,
                             @RequestParam(required = false) String patrimonio){

        itemService.updateItem(id, nome, categoria, rm, patrimonio);

        return "redirect:/itens/cadastrados";
    }
}
