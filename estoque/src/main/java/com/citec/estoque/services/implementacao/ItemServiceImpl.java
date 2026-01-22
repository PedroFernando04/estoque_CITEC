package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.enums.EnumCategoriasItem;
import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemEstoqueRepository;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemPedidoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ItemEstoqueRepository itemEstoqueRepository;


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

    //delete
    public void removerItem(Item item){
        List<ItemPedido> pedidosVerificados = itemPedidoRepository.findByItem(item);
        List<ItemEstoque> estoquesVerificados = itemEstoqueRepository.findByItem(item);

        if(pedidosVerificados.isEmpty() && estoquesVerificados.isEmpty()){
            itemRepository.delete(item);
        } else throw new IllegalArgumentException("O item não pode ser excluído, pois está associado a um pedido ou estoque");
    }

    //update
    public void updateItem(Long id, String nome, EnumCategoriasItem categoria, String rm, String patrimonio){
        Optional<Item> item = itemRepository.findById(id);

        if(item.isPresent()){
            if (!nome.isBlank())
                item.get().setNome(nome);
            if (categoria != null)
                item.get().setCategoriaItem(categoria);
            if (!rm.isBlank())
                item.get().setCodigoRM(rm);
            if (!patrimonio.isBlank())
                item.get().setCodigoPatrimonio(patrimonio);

            itemRepository.save(item.get());
        }
        else throw new IllegalArgumentException("Item não encontrado");
    }
}
