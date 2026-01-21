package com.citec.estoque.services.implementacao;

import com.citec.estoque.entities.enums.EnumStatusMovimentacao;
import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import com.citec.estoque.entities.tabelasAuxiliares.Movimentacao;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemEstoqueRepository;
import com.citec.estoque.repositorys.tabelasAuxiliares.MovimentacaoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.EstoqueRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.services.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class EstoqueServiceImpl implements EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ItemEstoqueRepository itemEstoqueRepository;

    //CREATE
    public void salvarEstoque(Estoque estoque) {
        Optional<Estoque> estoqueRetorno = estoqueRepository.findByNomeIgnoreCase(estoque.getNome());

        if (estoqueRetorno.isPresent())
            throw new IllegalArgumentException("Nome Duplicado");

        else
            estoqueRepository.save(estoque);
    }

    //INSERT
    public void inserirItem(String itemNome, Integer quantidade, Long id) {

        Optional<Item> item = itemRepository.findByNomeIgnoreCase(itemNome);
        Optional<Estoque> estoque = estoqueRepository.findById(id);

        Optional<ItemEstoque> itemEstoqueLocal = itemEstoqueRepository.findByEstoqueIdAndItemId(id, item.get().getId());
        if (itemEstoqueLocal.isPresent()) {
            throw new IllegalArgumentException("Item já cadastrado neste local");
        }

        ItemEstoque itemEstoque = new ItemEstoque();

        itemEstoque.setQuantidade(quantidade);
        itemEstoque.setEstoque(estoque.get());
        itemEstoque.setItem(item.get());

        itemEstoqueRepository.save(itemEstoque);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setData(LocalDateTime.now());
        movimentacao.setItem(item.get());
        movimentacao.setQuantidade(quantidade);
        movimentacao.setDestino(estoque.get());
        movimentacao.setStatus(EnumStatusMovimentacao.ENTRADA);
        movimentacaoRepository.save(movimentacao);
    }


}
