package com.citec.estoque.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItensComQuantidadeDTO {

    private Long id;
    private String nomeFantasia;
    private String nome;
    private String codigoRM;
    private Integer total;
    private Integer emUso;
    private Integer disponivel;
    private String imagePath;
    private Boolean faltando;


    //Getter
    public String getCodigoRMFormatado() {

        if (this.codigoRM == null || this.codigoRM.length() != 7) {
            return this.codigoRM;
        }

        return this.codigoRM.replaceAll(
                "(\\d{2})(\\d{2})(\\d{3})",
                "$1.$2.$3"
        );
    }
}
