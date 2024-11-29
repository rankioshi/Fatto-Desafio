package com.desafio.listadetarefas.servico;

import com.desafio.listadetarefas.DTO.TarefaRequesicaoDto;
import com.desafio.listadetarefas.DTO.TarefaRespostaDto;
import com.desafio.listadetarefas.modelo.Tarefa;
import com.desafio.listadetarefas.repositorio.TarefaRepositorio;
import jakarta.persistence.EntityExistsException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaServico {
    private final TarefaRepositorio tarefaRepositorio;

    public TarefaServico(TarefaRepositorio tarefaRepositorio) {
        this.tarefaRepositorio = tarefaRepositorio;
    }

    public void adicionarTarefa(TarefaRequesicaoDto tarefaRequesicaoDto) {
        if (tarefaRepositorio.findByNome(tarefaRequesicaoDto.nome()) != null) {
            throw new EntityExistsException("Uma tarefa com este nome já existe");
        }
        Tarefa tarefa = new Tarefa();
        tarefa.setNome(tarefaRequesicaoDto.nome());
        tarefa.setValor(tarefaRequesicaoDto.valor());
        tarefa.setDataLimite(tarefaRequesicaoDto.dataLimite());

        Integer maxOrdem = tarefaRepositorio.findMaxOrdemDeApresentacao();
        Integer novaOrdem;
        if (maxOrdem == null) {
            novaOrdem = 1;
        }else{
            novaOrdem = maxOrdem + 1;
        }
        tarefa.setOrdemDeApresentacao(novaOrdem);
        tarefaRepositorio.save(tarefa);
    }

    public void editarTarefa(Long id, TarefaRequesicaoDto tarefaRequesicaoDto) {
        Tarefa tarefa = tarefaRepositorio.findById(id).orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

        Tarefa tarefaComMesmoNome = tarefaRepositorio.findByNome(tarefaRequesicaoDto.nome());
        if (tarefaComMesmoNome != null && !tarefaComMesmoNome.getId().equals(tarefa.getId())) {
            throw new EntityExistsException("Uma tarefa com este nome já existe");
        }

        tarefa.setNome(tarefaRequesicaoDto.nome());
        tarefa.setValor(tarefaRequesicaoDto.valor());
        tarefa.setDataLimite(tarefaRequesicaoDto.dataLimite());
        tarefaRepositorio.save(tarefa);
    }


    public void excluirTarefa(Long id) {
        if (!tarefaRepositorio.existsById(id)) {
            throw new IllegalArgumentException("Tarefa não encontrada");
        }
        tarefaRepositorio.deleteById(id);
    }

    public void moverTarefa(Long id, boolean moverParaCima, boolean moverParaBaixo) {
        Tarefa tarefa = tarefaRepositorio.findById(id).orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));
        int ordemAtual = tarefa.getOrdemDeApresentacao();

        if (moverParaCima) {
            Tarefa tarefaAdjacente = tarefaRepositorio.findByOrdemDeApresentacao(ordemAtual - 1);
            if (tarefaAdjacente != null) {
                trocarOrdem(tarefa, tarefaAdjacente);
            }
        } else if (moverParaBaixo) {
            Tarefa tarefaAdjacente = tarefaRepositorio.findByOrdemDeApresentacao(ordemAtual + 1);
            if (tarefaAdjacente != null) {
                trocarOrdem(tarefa, tarefaAdjacente);
            }
        }
    }

    public void arrastarTarefa(Long tarefaId, Long tarefa2Id) {
        Tarefa tarefaParaMover = tarefaRepositorio.findById(tarefaId)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

        int ordemAtualTarefaParaMover = tarefaParaMover.getOrdemDeApresentacao();

        Tarefa tarefaEmpilhada = tarefaRepositorio.findById(tarefa2Id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

        int ordemTarefaEmpilhada = tarefaEmpilhada.getOrdemDeApresentacao();


        if (ordemAtualTarefaParaMover > ordemTarefaEmpilhada) {

            List<Tarefa> tarefasParaDeslocar = tarefaRepositorio.findByOrdemDeApresentacaoBetween(
                    ordemTarefaEmpilhada, ordemAtualTarefaParaMover - 1);

            for (Tarefa tarefa : tarefasParaDeslocar) {
                tarefa.setOrdemDeApresentacao(tarefa.getOrdemDeApresentacao() + 1);
                tarefaRepositorio.save(tarefa);
            }

            tarefaParaMover.setOrdemDeApresentacao(ordemTarefaEmpilhada);
        } else if (ordemAtualTarefaParaMover < ordemTarefaEmpilhada) {

            List<Tarefa> tarefasParaDeslocar = tarefaRepositorio.findByOrdemDeApresentacaoBetween(
                    ordemAtualTarefaParaMover + 1, ordemTarefaEmpilhada);

            for (Tarefa tarefa : tarefasParaDeslocar) {
                tarefa.setOrdemDeApresentacao(tarefa.getOrdemDeApresentacao() - 1);
                tarefaRepositorio.save(tarefa);
            }

            tarefaParaMover.setOrdemDeApresentacao(ordemTarefaEmpilhada);
        }

        tarefaRepositorio.save(tarefaParaMover);
    }

    public List<TarefaRespostaDto> getAll() {
        return tarefaRepositorio.findAll(Sort.by(Sort.Order.asc("ordemDeApresentacao")))
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }


    private TarefaRespostaDto converterParaDTO(Tarefa tarefa) {
        return new TarefaRespostaDto(
                tarefa.getId(),
                tarefa.getNome(),
                tarefa.getValor(),
                tarefa.getDataLimite()
        );
    }

    private void trocarOrdem(Tarefa tarefa1, Tarefa tarefa2) {
        Integer tempOrdem = tarefa1.getOrdemDeApresentacao();
        tarefa1.setOrdemDeApresentacao(tarefa2.getOrdemDeApresentacao());
        tarefa2.setOrdemDeApresentacao(tempOrdem);

        tarefaRepositorio.save(tarefa1);
        tarefaRepositorio.save(tarefa2);
    }


}
