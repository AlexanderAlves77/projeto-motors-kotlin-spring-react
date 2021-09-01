import React, { useState } from 'react';
import moment from 'moment';
import { Modal } from 'react-bootstrap';
import listaVazia from '../icones/lista-vazia.svg';
import { Item } from './Item';
import { executaRequisicao } from '../../services/api';

export const Listagem = props => {
  const { tarefas, getTarefasComFiltro } = props;

  // STATE DE EXIBIÇÃO DO MODAL
  const [showModal, setShowModal] = useState(false);

  // STATES DO MODAL
  const [erro, setErro] = useState('');
  const [idTarefa, setIdTarefa] = useState('');
  const [nomeTarefa, setNomeTarefa] = useState('');
  const [dataPrevisaoTarefa, setDataPrevisaoTarefa] = useState('');
  const [dataConclusao, setDataConclusao] = useState('');

  const atualizarTarefa = async () => {
    try {
      if (!nomeTarefa || !dataPrevisaoTarefa) {
        setErro('Favor informar nome e data de previsão');
        return;
      }

      const body = {
        nome: nomeTarefa,
        dataPrevisaoConclusao: dataPrevisaoTarefa,
        dataConclusao: dataConclusao,
      };

      await executaRequisicao('tarefa/' + idTarefa, 'put', body);
      await getTarefasComFiltro();

      setNomeTarefa('');
      setDataPrevisaoTarefa('');
      setDataConclusao('');
      setIdTarefa(null);
      setShowModal(false);
      //
    } catch (e) {
      console.log(e);

      if (e?.response?.data?.erro) {
        setErro(e.response.data.erro);
      } else {
        setErro(
          'Não foi possível cadastrar a tarefa, fale com o administrador.'
        );
      }
    }
  };

  const excluirTarefa = async () => {
    try {
      if (!idTarefa) {
        setErro('Favor informar nome e data de previsão');
        return;
      }

      await executaRequisicao('tarefa/' + idTarefa, 'delete');
      await getTarefasComFiltro();

      setDataPrevisaoTarefa('');
      setDataConclusao('');
      setIdTarefa(null);
      setShowModal(false);
      //
    } catch (e) {
      console.log(e);

      if (e?.response?.data?.erro) {
        setErro(e.response.data.erro);
      } else {
        setErro('Não foi possível excluir a tarefa, fale com o administrador.');
      }
    }
  };

  const selecionarTarefa = tarefa => {
    setIdTarefa(tarefa.id);
    setNomeTarefa(tarefa.nome);
    setDataPrevisaoTarefa(
      moment(tarefa.dataPrevistaConclusao).format('yyyy-MM-DD')
    );
    setDataConclusao(tarefa.dataConclusao);
    setShowModal(true);
  };

  return (
    <>
      <div
        className={
          'container-listagem' + (tarefas && tarefas.length > 0 ? '' : 'vazia')
        }
      >
        {tarefas && tarefas.length > 0 ? (
          tarefas?.map(tarefa => (
            <Item
              tarefa={tarefa}
              key={tarefa.id}
              selecionarTarefa={selecionarTarefa}
            />
          ))
        ) : (
          <>
            <img src={listaVazia} alt="Nenhuma atividade encontrada" />
            <p>Você ainda não possui tarefas cadastradas!</p>
          </>
        )}
      </div>
      <Modal
        show={showModal}
        onHide={() => setShowModal(false)}
        className="container-modal"
      >
        <Modal.Body>
          <p>Alterar uma tarefa</p>
          {erro && <p className="error">{erro}</p>}
          <input
            type="text"
            name="nome"
            placeholder="Digite o nome da tarefa"
            className="col-12"
            value={nomeTarefa}
            onChange={evt => setNomeTarefa(evt.target.value)}
          />
          <input
            type="text"
            name="dataPrevisao"
            placeholder="Digite a data conclusão"
            value={dataPrevisaoTarefa}
            onChange={evt => setDataPrevisaoTarefa(evt.target.value)}
            onFocus={evt => evt.target.type === 'date'}
            onBlur={evt =>
              dataPrevisaoTarefa
                ? evt.target.type === 'date'
                : evt.target.type === 'text'
            }
          />
          <input
            type="text"
            name="dataPrevisao"
            placeholder="Digite a data conclusão"
            value={dataConclusao}
            onChange={evt => setDataConclusao(evt.target.value)}
            onFocus={evt => evt.target.type === 'date'}
            onBlur={evt =>
              dataConclusao
                ? evt.target.type === 'date'
                : evt.target.type === 'text'
            }
          />
        </Modal.Body>
        <Modal.Footer>
          <div className="buttons col-12">
            <button onClick={atualizarTarefa}>Atualizar</button>
            <span onClick={excluirTarefa}>Excluir tarefa</span>
          </div>
        </Modal.Footer>
      </Modal>
    </>
  );
};
