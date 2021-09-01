import React, { useEffect, useState } from 'react';
import { Modal } from 'react-bootstrap';

import { Header } from '../assets/componentes/Header';
import { Filtros } from '../assets/componentes/Filtros';
import { Listagem } from '../assets/componentes/Listagem';
import { Footer } from '../assets/componentes/Footer';
import { executaRequisicao } from '../services/api';

export const Home = props => {
  // STATES DA LISTA
  const [tarefas, setTarefas] = useState([]);

  // STATES DOS FILTROS
  const [periodoDe, setPeriodoDe] = useState('');
  const [periodoAte, setPeriodoAte] = useState('');
  const [status, setStatus] = useState(0);

  // STATE DE EXIBIÇÃO DO MODAL
  const [showModal, setShowModal] = useState(false);

  // STATES DO MODAL
  const [erro, setErro] = useState('');
  const [nomeTarefa, setNomeTarefa] = useState('');
  const [dataPrevisaoTarefa, setDataPrevisaoTarefa] = useState('');

  //const toggleModal = () => setShowModal(!showModal);

  const getTarefasComFiltro = async () => {
    try {
      let filtros = '?status=' + status;

      if (periodoDe) {
        filtros += '&periodoDe=' + periodoDe;
      }

      if (periodoAte) {
        filtros += '&periodoAte=' + periodoAte;
      }

      const resultado = await executaRequisicao('tarefa' + filtros, 'get');

      if (resultado && resultado.data) {
        setTarefas(resultado.data);
      }
    } catch (e) {
      console.log(e);
    }
  };

  const salvarTarefa = async () => {
    try {
      if (!nomeTarefa || !dataPrevisaoTarefa) {
        setErro('Favor informar nome e data de previsão');
        return;
      }

      const body = {
        nome: nomeTarefa,
        dataPrevisaoConclusao: dataPrevisaoTarefa,
      };

      await executaRequisicao('tarefa', 'post', body);
      await getTarefasComFiltro();

      setNomeTarefa('');
      setDataPrevisaoTarefa('');
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

  useEffect(() => {
    getTarefasComFiltro();
  }, [status, periodoDe, periodoAte]);

  const sair = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('usuarioNome');
    localStorage.removeItem('usuarioEmail');
    props.setAccessToken('');
  };

  return (
    <>
      <Header sair={sair} showModal={() => setShowModal(true)} />
      <Filtros
        periodoDe={periodoDe}
        periodoAte={periodoAte}
        status={status}
        setPeriodoDe={setPeriodoDe}
        setPeriodoAte={setPeriodoAte}
        setStatus={setStatus}
      />

      <Listagem tarefas={tarefas} getTarefasComFiltro={getTarefasComFiltro} />

      <Footer showModal={() => setShowModal(true)} />

      <Modal
        show={showModal}
        onHide={() => setShowModal(false)}
        className="container-modal"
      >
        <Modal.Body>
          <p>Adicionar tarefa</p>
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
            placeholder="Digite a data de previsão de conclusão"
            value={dataPrevisaoTarefa}
            onChange={evt => setDataPrevisaoTarefa(evt.target.value)}
            onFocus={evt => evt.target.type === 'date'}
            onBlur={evt =>
              dataPrevisaoTarefa
                ? evt.target.type === 'date'
                : evt.target.type === 'text'
            }
          />
        </Modal.Body>
        <Modal.Footer>
          <div className="buttons col-12">
            <button onClick={salvarTarefa}>Salvar</button>
            <span
              onClick={() => {
                setShowModal(false);
                setErro('');
                setNomeTarefa('');
                setDataPrevisaoTarefa('');
              }}
            >
              Cancelar
            </span>
          </div>
        </Modal.Footer>
      </Modal>
    </>
  );
};
