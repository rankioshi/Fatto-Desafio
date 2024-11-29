package com.desafio.listadetarefas.modelo;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tarefas")
public class Tarefa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private double valor;

    private LocalDate dataLimite;

    private Integer ordemDeApresentacao;

    public Tarefa() {
    }

    public Tarefa(Long id, String nome, double valor, LocalDate dataLimite, Integer ordemDeApresentacao) {
        this.id = id;
        this.nome = nome;
        this.dataLimite = dataLimite;
        this.ordemDeApresentacao = ordemDeApresentacao;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public Integer getOrdemDeApresentacao() {
        return ordemDeApresentacao;
    }

    public void setOrdemDeApresentacao(Integer ordemDeApresentacao) {
        this.ordemDeApresentacao = ordemDeApresentacao;
    }
}
