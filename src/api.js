const URL = "http://localhost:8080";

const adicionarTarefa = async (tarefaRequisicao) => {
    try {
        const response = await fetch(`${URL}/tarefas/adicionar`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(tarefaRequisicao),
        });

        const mensagem = await response.text();

        if (response.ok) {
            alert(mensagem || "Tarefa adicionada com sucesso!");
        } else {
            alert(mensagem || "Erro ao adicionar a tarefa.");
        }
    } catch (error) {
        alert("Não foi possível salvar a tarefa. Tente novamente.");
    }
};

const editarTarefa = async (id, tarefaRequisicao) => {
    try {
        const response = await fetch(`${URL}/tarefas/editar?id=${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(tarefaRequisicao),
        });

        const mensagem = await response.text();

        if (response.ok) {
            alert(mensagem || "Tarefa editada com sucesso!");
        } else {
            alert(mensagem || "Erro ao editar a tarefa.");
        }
    } catch (error) {
        alert("Não foi possível editar a tarefa. Tente novamente.");
    }
};

const deletarTarefa = async (id) => {
    try {
        const response = await fetch(`${URL}/tarefas/deletar?id=${id}`, {
            method: "DELETE",
        });

        const mensagem = await response.text();

        if (response.ok) {
            alert(mensagem || "Tarefa deletada com sucesso!");
        } else {
            alert(mensagem || "Erro ao deletar a tarefa.");
        }
    } catch (error) {
        alert("Não foi possível deletar a tarefa. Tente novamente.");
    }
};

const moverTarefa = async (id, moverParaCima, moverParaBaixo) => {
    try {
        const response = await fetch(
            `${URL}/tarefas/mover?id=${id}&moverParaCima=${moverParaCima}&moverParaBaixo=${moverParaBaixo}`,
            { method: "POST" }
        );

        const mensagem = await response.text();

        if (response.ok) {
            alert(mensagem || "Tarefa movida com sucesso!");
        } else {
            alert(mensagem || "Erro ao mover a tarefa.");
        }
    } catch (error) {
        alert("Não foi possível mover a tarefa. Tente novamente.");
    }
};

const getAllTarefas = async () => {
    try {
        const response = await fetch(`${URL}/tarefas`, { method: "GET" });

        if (response.ok) {
            const tarefas = await response.json();
            return tarefas;
        } else {
            const mensagem = await response.text();
            alert(mensagem || "Erro ao carregar tarefas.");
        }
    } catch (error) {
        alert("Não foi possível carregar as tarefas. Tente novamente.");
    }
};

const arrastarTarefa = async (tarefa1Id, tarefa2Id) => {
    try{
        const response = await fetch(`${URL}/tarefas/arrastar?tarefa1Id=${tarefa1Id}&tarefa2Id=${tarefa2Id}`, {method: "POST"});
        if(response.ok){
            alert("Tarefa movida com sucesso");
        }
    }catch(error){
        alert("Não foi possível mover a tarefa. Tente novamente");
    }
}

export {getAllTarefas, adicionarTarefa, editarTarefa, deletarTarefa, moverTarefa, arrastarTarefa}