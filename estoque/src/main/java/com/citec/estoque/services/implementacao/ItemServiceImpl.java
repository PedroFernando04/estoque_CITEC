package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
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

    public String limparRm(String rm) {
        return rm.replaceAll("\\D", "");
    }

    public void salvarItem(Item item){
        Optional<Item> nomeDuplicado = itemRepository.findByNomeIgnoreCase(item.getNome());
        Optional<Item> rmDuplicado = itemRepository.findByCodigoRM(item.getCodigoRM());

        if(nomeDuplicado.isPresent()){
            throw new IllegalArgumentException("Nome já cadastrado");
        } else if (rmDuplicado.isPresent() && !item.getCodigoRM().equals("")) {
            throw new IllegalArgumentException("Código RM já cadastrado");
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
        } else {
            if(!estoquesVerificados.isEmpty()){
                List<ItemEstoque> estoquesAssociados = itemEstoqueRepository.findByItem(item);
                List<Estoque> estoques = estoquesAssociados.stream()
                        .map(ItemEstoque::getEstoque)
                        .distinct()
                        .toList();
                List<String> nomesEstoques = estoques.stream()
                        .map(Estoque::getNome)
                        .toList();

                String projetos = String.join(", ", nomesEstoques);

                throw new IllegalArgumentException("O item não pode ser excluído, pois está associado ao(s) seguinte(s) projeto(s): " + projetos);
            }

            if(!pedidosVerificados.isEmpty()){
                List<ItemPedido> pedidosAssociados = itemPedidoRepository.findByItem(item);
                List<Pedido> pedidos = pedidosAssociados.stream()
                        .map(ItemPedido::getPedido)
                        .distinct()
                        .toList();
                List<Long> nomesPedidos = pedidos.stream()
                        .map(Pedido::getId)
                        .toList();

                throw new IllegalArgumentException("O item não pode ser excluído, pois está associado ao(s) seguinte(s) pedido(s): " + nomesPedidos);
            }
        }
    }

    //update
    public void updateItem(Long id, String nome,  String rm){
        Optional<Item> item = itemRepository.findById(id);

        if(item.isPresent()){
            if (!nome.isBlank())
                item.get().setNome(nome);
            if (!rm.isBlank())
                item.get().setCodigoRM(rm);

            itemRepository.save(item.get());
        }
        else throw new IllegalArgumentException("Item não encontrado");
    }

    public void adicionarUmItemPedido(Long itemPedidoId){
        ItemPedido item = itemPedidoRepository.findById(itemPedidoId).orElse(null);

        item.setQuantidade(item.getQuantidade() + 1);

        itemPedidoRepository.save(item);
    }

    public void removerUmItemPedido(Long itemPedidoId){
        ItemPedido item = itemPedidoRepository.findById(itemPedidoId).orElse(null);

        if(item.getQuantidade() > 0){
            item.setQuantidade(item.getQuantidade() - 1);

            itemPedidoRepository.save(item);
        } else throw new IllegalArgumentException("O item não pode ficar com quantidade negativa");
    }

    public void adicionarUmItemEstoque(Long itemEstoqueId){
        ItemEstoque item = itemEstoqueRepository.findById(itemEstoqueId).orElse(null);

        item.setQuantidade(item.getQuantidade() + 1);

        itemEstoqueRepository.save(item);
    }

    public void removerUmItemEstoque(Long itemEstoqueId){
        ItemEstoque item = itemEstoqueRepository.findById(itemEstoqueId).orElse(null);

        if(item.getQuantidade() > 0){
            item.setQuantidade(item.getQuantidade() - 1);

            itemEstoqueRepository.save(item);
        } else throw new IllegalArgumentException("O item não pode ficar com quantidade negativa");
    }
}
