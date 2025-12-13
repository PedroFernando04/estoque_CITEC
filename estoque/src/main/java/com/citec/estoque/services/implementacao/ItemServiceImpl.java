package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public void salvarItem(Item item){
        Optional<Item> nomeDuplicado = itemRepository.findByNomeIgnoreCase(item.getNome());
        Optional<Item> rmDuplicado = itemRepository.findByCodigoRM(item.getCodigoRM());
        Optional<Item> patrimonioDuplicado = itemRepository.findByCodigoPatrimonio(item.getCodigoPatrimonio());

        if(nomeDuplicado.isPresent()){
            throw new IllegalArgumentException("Nome já cadastrado");
        } else if (rmDuplicado.isPresent()) {
            throw new IllegalArgumentException("Código RM já cadastrado");
        }else if (patrimonioDuplicado.isPresent()) {
            throw new IllegalArgumentException("Código de Patrimônio já cadastrado");
        } else  {
            itemRepository.save(item);
        }
    }

}
