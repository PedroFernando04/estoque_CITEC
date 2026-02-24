package com.citec.estoque.services.implementacao;

import com.citec.estoque.dtos.ItensComQuantidadeDTO;
import com.citec.estoque.entities.tabelasAuxiliares.ItemEstoque;
import com.citec.estoque.entities.tabelasAuxiliares.ItemPedido;
import com.citec.estoque.entities.tabelasAuxiliares.Movimentacao;
import com.citec.estoque.entities.tabelasPrincipais.Estoque;
import com.citec.estoque.entities.tabelasPrincipais.Item;
import com.citec.estoque.entities.tabelasPrincipais.Pedido;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemEstoqueRepository;
import com.citec.estoque.repositorys.tabelasAuxiliares.ItemPedidoRepository;
import com.citec.estoque.repositorys.tabelasAuxiliares.MovimentacaoRepository;
import com.citec.estoque.repositorys.tabelasPrincipais.ItemRepository;
import com.citec.estoque.services.ItemService;
import com.citec.estoque.specification.tabelasAuxiliares.ItemEstoqueSpecification;
import com.citec.estoque.specification.tabelasPrincipais.ItemSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ItemEstoqueRepository itemEstoqueRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    public String limparRm(String rm) {
        return rm.replaceAll("\\D", "");
    }

    public void salvarItem(Item item){
        Optional<Item> nomeDuplicado = itemRepository.findByNomeIgnoreCase(item.getNome());
        Optional<Item> rmDuplicado = itemRepository.findByCodigoRM(item.getCodigoRM());
        Optional<Item> nomeFantasiaDuplicado = itemRepository.findByNomeFantasiaIgnoreCase(item.getNomeFantasia());

        if(nomeDuplicado.isPresent() && !item.getNome().equals("")){
            throw new IllegalArgumentException("Nome já cadastrado");
        } else if (rmDuplicado.isPresent() && !item.getCodigoRM().equals("")) {
            throw new IllegalArgumentException("Código RM já cadastrado");
        } else if (nomeFantasiaDuplicado.isPresent()) {
            throw new IllegalArgumentException("Nome Fantasia já cadastrado");
        } else  {
            itemRepository.save(item);
        }
    }

    //delete
    public void removerItem(Item item){
        List<ItemPedido> pedidosVerificados = itemPedidoRepository.findByItem(item);
        List<ItemEstoque> estoquesVerificados = itemEstoqueRepository.findByItem(item);

        if(pedidosVerificados.isEmpty() && estoquesVerificados.size() == 1){

            List<Movimentacao> movimentacaos = movimentacaoRepository.findByItemId(item.getId());
            movimentacaoRepository.deleteAll(movimentacaos);

            Optional<ItemEstoque> itemEstoque = itemEstoqueRepository.findByEstoqueIdAndItemId(1L, item.getId());
            itemEstoqueRepository.delete(itemEstoque.get());

            excluirFoto(item.getImagePath());
            
            itemRepository.delete(item);
        } else {
            if(!estoquesVerificados.isEmpty() && estoquesVerificados.size() > 1) {
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
                List<String> nomesPedidos = pedidos.stream()
                        .map(Pedido::getTitulo)
                        .toList();

                String projetos = String.join(", ", nomesPedidos);

                throw new IllegalArgumentException("O item não pode ser excluído, pois está associado ao(s) seguinte(s) pedido(s): " + projetos);
            }
        }
    }

    //update
    public void updateItem(Long id, String nomeFantasia, String nome,  String rm, Integer quantidade, MultipartFile foto) {
        Optional<Item> item = itemRepository.findById(id);

        if(item.isPresent()){
            if (!nomeFantasia.isBlank())
                item.get().setNomeFantasia(nomeFantasia);
            if (!nome.isBlank())
                item.get().setNome(nome);
            if (!rm.isBlank())
                item.get().setCodigoRM(rm);
            if (quantidade != null){
                Optional<ItemEstoque> itemEstoque = itemEstoqueRepository.findByEstoqueIdAndItemId(1L, id);
                itemEstoque.get().setQuantidade(quantidade);
            }
            if (!foto.isEmpty()) {
                excluirFoto(item.get().getImagePath());

                item.get().setImagePath(salvarFoto(foto));
            }


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


    public String salvarFoto(MultipartFile file){
        final String DIRETORIO_UPLOAD = "uploads/";

        try {
            // 1. Cria o diretório caso ele não exista
            Path caminhoDiretorio = Paths.get(DIRETORIO_UPLOAD);
            if (!Files.exists(caminhoDiretorio)) {
                Files.createDirectories(caminhoDiretorio);
            }
            //Criar index.html para evitar que a pasta seja exposta pelo url
            Path arquivoIndex = caminhoDiretorio.resolve("index.html");
            if (!Files.exists(arquivoIndex)) {
                Files.createFile(arquivoIndex);
                // Opcional: Escrever algo dentro para garantir que não listem nada
                // Files.writeString(arquivoIndex, "<html><body></body></html>");
            }

            String nomeOriginal = file.getOriginalFilename().replace(" ", "_");
            // Isso transformará "Captura de tela.png" em "Captura_de_tela.png"

            // 2. Gera um nome único para o arquivo (evita sobrescrever arquivos com mesmo nome)
            String nomeArquivo = UUID.randomUUID().toString() + "_" + nomeOriginal;

            // 3. Define o caminho completo do arquivo (Ex: uploads/1234-5678_foto.jpg)
            Path caminhoArquivo = caminhoDiretorio.resolve(nomeArquivo);

            // 4. Copia o arquivo recebido para a pasta física
            Files.copy(file.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);

            return  caminhoArquivo.toString();

        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public Integer calcularTotalItemEstoque (Item item){
        List<ItemEstoque> totalItemEstoque = itemEstoqueRepository.findByItem(item);
        Integer total = totalItemEstoque.stream().mapToInt(ItemEstoque::getQuantidade).sum();

        return total;
    }

    public Integer calcularTotalDisponivel(Long estoqueId, Long itemId){
        Optional<ItemEstoque> totalItemEstoqueCITEC = itemEstoqueRepository.findByEstoqueIdAndItemId(estoqueId, itemId);
        Integer totalDisponivel = totalItemEstoqueCITEC.get().getQuantidade();

        return totalDisponivel;
    }

    public Page<ItensComQuantidadeDTO> listarItensComQuantidade(Specification<Item> spec, Pageable page){

        Page<Item> paginaItens = itemRepository.findAll(spec, page);

        List<ItensComQuantidadeDTO> dtos = paginaItens.getContent().stream()
                .map(item ->{
                    Integer total = calcularTotalItemEstoque(item);

                    Integer totalDisponivel = calcularTotalDisponivel(1L, item.getId());

                    Integer totalEmUso = total - totalDisponivel;

                    ItensComQuantidadeDTO dto = new ItensComQuantidadeDTO();
                    dto.setId(item.getId());
                    dto.setNomeFantasia(item.getNomeFantasia());
                    dto.setNome(item.getNome());
                    dto.setCodigoRM(item.getCodigoRM());
                    dto.setTotal(total);
                    dto.setEmUso(totalEmUso);
                    dto.setDisponivel(totalDisponivel);
                    dto.setImagePath(item.getImagePath());
                    dto.setFaltando(item.getFaltando());

                    return dto;

                }).toList();

        return new PageImpl<>(
                dtos,
                page,
                paginaItens.getTotalElements());
    }

    public void excluirFoto(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        try {
            Path caminho = Paths.get(imagePath);
            // Deleta o arquivo se ele existir na pasta
            Files.deleteIfExists(caminho);
            System.out.println("Arquivo excluído com sucesso: " + imagePath);
        } catch (IOException e) {
            // Log de erro, mas não necessariamente trava o sistema
            System.err.println("Erro ao excluir arquivo físico: " + e.getMessage());
        }
    }

    public ItensComQuantidadeDTO buscarItemComQuantidade(Long id){
        Optional<Item> item = itemRepository.findById(id);

        if(item.isPresent()){
            Integer total = calcularTotalItemEstoque(item.get());

            Integer totalDisponivel = calcularTotalDisponivel(1L, item.get().getId());

            Integer totalEmUso = total - totalDisponivel;

            ItensComQuantidadeDTO dto = new ItensComQuantidadeDTO();
            dto.setId(item.get().getId());
            dto.setNome(item.get().getNome());
            dto.setNomeFantasia(item.get().getNomeFantasia());
            dto.setCodigoRM(item.get().getCodigoRM());
            dto.setTotal(total);
            dto.setEmUso(totalEmUso);
            dto.setDisponivel(totalDisponivel);
            dto.setImagePath(item.get().getImagePath());
            dto.setFaltando(item.get().getFaltando());

            return dto;
        } else {
            return null;
        }
    }

    public void atualizarItemFaltando(Long itemId){
        Optional<Item> item = itemRepository.findById(itemId);

        if (item.isPresent()) {
            if (item.get().getFaltando())
                item.get().setFaltando(false);
            else
                item.get().setFaltando(true);

            itemRepository.save(item.get());
        } else throw new IllegalArgumentException("Relação ItemEstoque não encontrada");
    }
}