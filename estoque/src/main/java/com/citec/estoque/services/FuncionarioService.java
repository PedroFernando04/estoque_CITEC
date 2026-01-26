package com.citec.estoque.services;

import com.citec.estoque.entities.enums.EnumCleroFuncionario;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;

public interface FuncionarioService {

    public void salvarFuncionario(Funcionario funcionario);

    public void deletarFuncionario(Long funcionarioId);

    public void updateFuncionario(Long id, String nome, EnumCleroFuncionario clero);
}
