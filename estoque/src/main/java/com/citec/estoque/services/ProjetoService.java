package com.citec.estoque.services;

import com.citec.estoque.entities.enums.EnumStatusProjeto;
import com.citec.estoque.entities.tabelasPrincipais.Projeto;

import java.util.List;

public interface ProjetoService {

    public void salvarProjeto(Projeto projeto,  List<Long> funcionariosIds);

    public void atualizarProjeto(Long id, String nome, String solicitante, EnumStatusProjeto status, List<Long> funcionariosId);
}
