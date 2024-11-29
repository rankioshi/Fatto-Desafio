package com.desafio.listadetarefas.DTO;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.desafio.listadetarefas.modelo.Tarefa}
 */
public record TarefaRequesicaoDto(String nome, double valor, LocalDate dataLimite) implements Serializable {
}