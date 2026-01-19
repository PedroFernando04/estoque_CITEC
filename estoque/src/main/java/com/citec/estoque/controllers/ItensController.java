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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping
public class ItensController {

    @Autowired
    private ItemEstoqueRepository itemEstoqueRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;


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
}
