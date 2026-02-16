package com.citec.estoque.services;

import com.citec.estoque.dtos.ItensComQuantidadeDTO;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

public interface ItemService {

    public String limparRm(String rm);

    public void salvarItem(Item item);

    public void removerItem(Item item);

    public void updateItem(Long id, String nomeFantasia, String nome,  String rm, Integer quantidade, MultipartFile foto);

    public void adicionarUmItemEstoque(Long item);

    public void removerUmItemEstoque(Long item);

    public void adicionarUmItemPedido(Long item);

    public void removerUmItemPedido(Long item);

    public String salvarFoto(MultipartFile file);

    public Page<ItensComQuantidadeDTO> listarItensComQuantidade(Specification<Item> spec, Pageable page);

    public void excluirFoto(String imagePath);

    public ItensComQuantidadeDTO buscarItemComQuantidade(Long id);

    public Integer calcularTotalItemEstoque (Item item);

    public Integer calcularTotalDisponivel(Long estoqueId, Long itemId);

    public void atualizarItemFaltando(Long itemId);

}
