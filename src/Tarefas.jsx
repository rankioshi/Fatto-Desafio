import { useEffect, useState } from "react";
import { adicionarTarefa, deletarTarefa, editarTarefa, getAllTarefas, arrastarTarefa, moverTarefa } from "./api";
import './Tarefas.css';
import setaCima from './assets/SetaCima.svg';
import setaBaixo from './assets/setaBaixo.svg';
import editar_icon from './assets/editar_icon.svg';

const Tarefas = () => {
    const [tarefas, setTarefas] = useState([]);
    const [mostrarForm, setMostrarForm] = useState(false);
    const [mostrarPopUp, setMostrarPopUp] = useState(false);
    const [tarefaAtual, setTarefaAtual] = useState(null);
    const [nome, setNome] = useState("");
    const [valor, setValor] = useState("");
    const [dataLimite, setDataLimite] = useState("");

    const handleGetAllTarefas = async () => {
        const response = await getAllTarefas();
        if (Array.isArray(response)) {
            setTarefas(response);
        }
    };

    useEffect(() => {
        const fetchTarefasIntervalo = setInterval(() => {
          if (tarefas.length === 0) {
            handleGetAllTarefas();
          }
        }, 5000);
      
      
        return () => clearInterval(fetchTarefasIntervalo);
      }, [tarefas]);
      

    const handleEditarTarefa = async () => {
        const tarefaRequesicao = { nome, valor, dataLimite };
        await editarTarefa(tarefaAtual.id, tarefaRequesicao);
        setMostrarForm(false);
        setTarefaAtual(null);
        handleGetAllTarefas();
    };

    const handleDeletarTarefa = async () => {
        if (tarefaAtual) {
            await deletarTarefa(tarefaAtual.id);
            setMostrarPopUp(false);
            setTarefaAtual(null);
            handleGetAllTarefas();
        }
    };

    const handleMoverTarefa = async (id, moverParaCima, moverParaBaixo) => {
        await moverTarefa(id, moverParaCima, moverParaBaixo);
    };

    const handleAdicionarTarefa = async () => {
        const tarefaRequesicao = { nome, valor, dataLimite };
        await adicionarTarefa(tarefaRequesicao);
        setMostrarForm(false);
        handleGetAllTarefas();
    };

    const handleDragStart = (e, tarefaId) => {
        e.dataTransfer.setData("tarefaId", tarefaId);
    };

    const handleDrop = async (e, tarefaId) => {
        const tarefaArrastadaId = e.dataTransfer.getData("tarefaId");
        if (tarefaArrastadaId && tarefaArrastadaId !== tarefaId) {
            await arrastarTarefa(tarefaArrastadaId, tarefaId);
            handleGetAllTarefas();
        }
    };

    const handleDragOver = (e) => {
        e.preventDefault();
    };

    useEffect(() => {
        handleGetAllTarefas();
    }, [tarefas]);

    return (
        <>
            <h1>Lista de Tarefas</h1>
            <h2 className="sub-title">Clique ou arraste a tarefa para mudar a ordem</h2>
            <div className="container">
                {tarefas.map((tarefa, index) => (
                    <div
                        className="card"
                        key={tarefa.id}
                        draggable
                        onDragStart={(e) => handleDragStart(e, tarefa.id)}
                        onDrop={(e) => handleDrop(e, tarefa.id)}
                        onDragOver={handleDragOver}
                    >
                        <h2>{tarefa.nome}</h2>
                        <p className={`valor${tarefa.valor >= 1000 ? "-maior" : ""}`}>Valor: R${tarefa.valor}</p>
                        <p>Data Limite: {tarefa.dataLimite}</p>
                        <div className="botoes">
                            <button 
                                className={`cima${index === 0 ? "-disabled" : ""}`} 
                                disabled={index === 0} 
                                onClick={() => handleMoverTarefa(tarefa.id, true, false)}
                            >
                                <img src={setaCima} alt="seta" />
                            </button>
                            <button 
                                className={`baixo${index === tarefas.length - 1 ? "-disabled" : ""}`} 
                                disabled={index === tarefas.length - 1} 
                                onClick={() => handleMoverTarefa(tarefa.id, false, true)}
                            >
                                <img src={setaBaixo} alt="seta" />
                            </button>
                        </div>
                        <div className="botoes-operacoes">
                            <button 
                                className="editar" 
                                onClick={() => { 
                                    setMostrarForm(true); 
                                    setTarefaAtual(tarefa); 
                                    setNome(tarefa.nome); 
                                    setValor(tarefa.valor); 
                                    setDataLimite(tarefa.dataLimite); 
                                }}
                            >
                                <img src={editar_icon} alt="editar" />
                            </button>
                            <button 
                                className="deletar" 
                                onClick={() => { 
                                    setMostrarPopUp(true); 
                                    setTarefaAtual(tarefa); 
                                }}
                            >
                                X
                            </button>
                        </div>
                    </div>
                ))}
                <button 
                    className="adicionar" 
                    onClick={() => { 
                        setMostrarForm(true); 
                        setTarefaAtual(null); 
                        setNome(""); 
                        setValor(""); 
                        setDataLimite(""); 
                    }}
                >
                    +
                </button>
            </div>

            {mostrarForm && (
                <div className="form">
                    <input type="text" value={nome} onChange={(e) => setNome(e.target.value)} placeholder="Nome" />
                    <input type="number" value={valor} onChange={(e) => setValor(e.target.value)} placeholder="Valor" />
                    <input type="date" value={dataLimite} onChange={(e) => setDataLimite(e.target.value)} />
                    <button onClick={tarefaAtual ? handleEditarTarefa : handleAdicionarTarefa}>
                        {tarefaAtual ? "Editar" : "Adicionar"}
                    </button>
                    <button onClick={() => setMostrarForm(false)}>Cancelar</button>
                </div>
            )}

            {mostrarPopUp && (
                <div className="popup">
                    <p>Tem certeza que deseja excluir essa tarefa?</p>
                    <button onClick={handleDeletarTarefa}>Sim</button>
                    <button onClick={() => setMostrarPopUp(false)}>Não</button>
                </div>
            )}
        </>
    );
};

export default Tarefas;
