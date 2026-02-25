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
import com.citec.estoque.repositorys.tabelasPrincipais.ProjetoRepository;
import com.citec.estoque.services.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


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

    @Autowired
    private ProjetoRepository projetoRepository;

    //CREATE
    public void salvarEstoque(Estoque estoque) {
        Optional<Estoque> estoqueRetorno = estoqueRepository.findByNomeIgnoreCase(estoque.getNome());

        if (estoqueRetorno.isPresent())
            throw new IllegalArgumentException("Nome Duplicado");

        else
            estoqueRepository.save(estoque);
    }

    //INSERT
    public void inserirItem(String itemNome, Integer quantidade, Long id, String itemOrigem) {

        Optional<Item> item = itemRepository.findByNomeIgnoreCase(itemNome);
        Optional<Estoque> estoque = estoqueRepository.findById(id);
        Optional<Estoque> estoqueOrigem = estoqueRepository.findByNomeIgnoreCase(itemOrigem);

        Optional<ItemEstoque> itemEstoqueLocal = itemEstoqueRepository.findByEstoqueIdAndItemId(id, item.get().getId());

        if(quantidade < 0){
            throw new IllegalArgumentException("O item não pode ser inserido com quantidade negativa");
        }

        if(itemOrigem != null && !itemOrigem.isBlank() && estoqueOrigem.isPresent()) {

            Optional<ItemEstoque> itemEstoqueBuscado = itemEstoqueRepository.findByEstoqueIdAndItemId(estoqueOrigem.get().getId(), item.get().getId());

            if (itemEstoqueBuscado.isPresent()){
                deletarItemEstoque(itemEstoqueBuscado.get().getId(), quantidade);
            } else throw new IllegalArgumentException("O item selecionado não está presente na origem informada");
        }

        if (itemEstoqueLocal.isPresent()) {
            itemEstoqueLocal.get().setQuantidade(itemEstoqueLocal.get().getQuantidade() + quantidade);
            itemEstoqueRepository.save(itemEstoqueLocal.get());

        } else {

            ItemEstoque itemEstoque = new ItemEstoque();

            itemEstoque.setQuantidade(quantidade);
            itemEstoque.setEstoque(estoque.get());
            itemEstoque.setItem(item.get());

            itemEstoqueRepository.save(itemEstoque);
        }

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setData(LocalDateTime.now());
        movimentacao.setItem(item.get());
        movimentacao.setQuantidade(quantidade);
        movimentacao.setDestino(estoque.get());
        if(itemOrigem == null || itemOrigem.isBlank()) {
            movimentacao.setOrigem(null);
            movimentacao.setStatus(EnumStatusMovimentacao.ENTRADA);
        } else {
            movimentacao.setOrigem(estoqueOrigem.get());
            movimentacao.setStatus(EnumStatusMovimentacao.MOVIMENTACAO);
        }
        movimentacaoRepository.save(movimentacao);
    }


    public void deletarItemEstoque(Long itemEstoqueId, Integer quantidade) {

        Optional<ItemEstoque> itemEstoque = itemEstoqueRepository.findById(itemEstoqueId);

        Movimentacao movimentacao = new Movimentacao();

        if (quantidade < 0)
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        if (quantidade > itemEstoque.get().getQuantidade())
            throw new IllegalArgumentException("Quantidade removida não pode ser maior que a estocada");

        if (quantidade.equals(itemEstoque.get().getQuantidade())) {
            movimentacao.setQuantidade(itemEstoque.get().getQuantidade());
            itemEstoqueRepository.deleteById(itemEstoqueId);
        } else {
            itemEstoque.get().setQuantidade(itemEstoque.get().getQuantidade() - quantidade);
            movimentacao.setQuantidade(quantidade);
            itemEstoqueRepository.save(itemEstoque.get());
        }

        movimentacao.setData(LocalDateTime.now());
        movimentacao.setItem(itemEstoque.get().getItem());
        movimentacao.setOrigem(itemEstoque.get().getEstoque());
        movimentacao.setStatus(EnumStatusMovimentacao.SAIDA);
        movimentacaoRepository.save(movimentacao);

    }

    public void atualizarEstoque(Long id, String nome) {
        Optional<Estoque> estoque = estoqueRepository.findById(id);

        if (estoque.isPresent()) {
            if (!nome.isBlank())
                estoque.get().setNome(nome);

            estoqueRepository.save(estoque.get());

        } else throw new IllegalArgumentException("Estoque não encontrado");
    }
}
