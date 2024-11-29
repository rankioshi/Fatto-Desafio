package com.desafio.listadetarefas.controller;

import com.desafio.listadetarefas.DTO.TarefaRequesicaoDto;
import com.desafio.listadetarefas.DTO.TarefaRespostaDto;
import com.desafio.listadetarefas.servico.TarefaServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    private final TarefaServico tarefaServico;

    public TarefaController(TarefaServico tarefaServico) {
        this.tarefaServico = tarefaServico;
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Object> adicionarTarefa(@RequestBody TarefaRequesicaoDto tarefaRequesicaoDto) {
        try {
            validarTarefaRequesicaoDto(tarefaRequesicaoDto);
            tarefaServico.adicionarTarefa(tarefaRequesicaoDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/editar")
    public ResponseEntity<Object> editarTarefa(@RequestParam Long id, @RequestBody TarefaRequesicaoDto tarefaRequesicaoDto) {
        try {
            validarTarefaRequesicaoDto(tarefaRequesicaoDto);
            tarefaServico.editarTarefa(id, tarefaRequesicaoDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> deletarTarefa(@RequestParam Long id) {
        tarefaServico.excluirTarefa(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/mover")
    public ResponseEntity<Void> moverTarefa(@RequestParam Long id, @RequestParam boolean moverParaCima,
                                            @RequestParam boolean moverParaBaixo) {
        try {
            tarefaServico.moverTarefa(id, moverParaCima, moverParaBaixo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TarefaRespostaDto>> getAllTarefas() {
        return ResponseEntity.ok().body(tarefaServico.getAll());
    }

    @PostMapping("/arrastar")
    public ResponseEntity<Void> arrastarTarefa(@RequestParam Long tarefa1Id, @RequestParam Long tarefa2Id) {
        try {
            tarefaServico.arrastarTarefa(tarefa1Id, tarefa2Id);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private void validarTarefaRequesicaoDto(TarefaRequesicaoDto tarefaRequesicaoDto) {
        if (tarefaRequesicaoDto.nome() == null || tarefaRequesicaoDto.nome().isBlank()) {
            throw new IllegalArgumentException("O nome da tarefa não pode ser vazio.");
        }
        if (tarefaRequesicaoDto.valor() <= 0) {
            throw new IllegalArgumentException("O valor da tarefa deve ser maior que zero.");
        }
        if (tarefaRequesicaoDto.dataLimite().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data limite não pode ser no passado.");
        }
    }
}
