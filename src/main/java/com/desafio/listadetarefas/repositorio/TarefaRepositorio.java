package com.desafio.listadetarefas.repositorio;

import com.desafio.listadetarefas.modelo.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepositorio extends JpaRepository<Tarefa, Long> {
    Tarefa findByNome(String nome);

    Tarefa findByOrdemDeApresentacao(Integer i);

    List<Tarefa> findByOrdemDeApresentacaoBetween(Integer ordemTarefaEmpilhada, Integer i);

    @Query("SELECT MAX(t.ordemDeApresentacao) FROM Tarefa t")
    Integer findMaxOrdemDeApresentacao();

}
