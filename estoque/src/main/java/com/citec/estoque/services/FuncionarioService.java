package com.citec.estoque.services;

import com.citec.estoque.entities.enums.EnumCargoFuncionario;
import com.citec.estoque.entities.tabelasPrincipais.Funcionario;

public interface FuncionarioService {

    public void salvarFuncionario(Funcionario funcionario, String senha);

    public void deletarFuncionario(Long funcionarioId);

    public void updateFuncionario(Long id, String nome, EnumCargoFuncionario cargo, String login, String senha);

    public boolean verifyPassword(String senhaDigitada, String senhaCriptografada);

    public Funcionario getFuncionarioByLogin(String login);
}
