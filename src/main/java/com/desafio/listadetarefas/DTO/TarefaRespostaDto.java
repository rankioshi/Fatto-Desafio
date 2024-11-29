package com.desafio.listadetarefas.DTO;

import com.desafio.listadetarefas.modelo.Tarefa;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Tarefa}
 */
public record TarefaRespostaDto(Long id, String nome, double valor, LocalDate dataLimite) implements Serializable {
}