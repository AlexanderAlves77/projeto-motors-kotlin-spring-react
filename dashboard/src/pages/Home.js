import React, { useState } from 'react';
import { Header } from '../assets/componentes/Header';
import { Filtros } from '../assets/componentes/Filtros';
import { Listagem } from '../assets/componentes/Listagem';
import { Footer } from '../assets/componentes/Footer';

export const Home = props => {
  const [tarefas, setTarefas] = useState([
    {
      id: '123wamaks',
      nome: 'Tarefa Mock 1',
      dataPrevisaoConclusao: '2021-08-31',
      dataConclusao: null,
    },
    {
      id: 'wamaks123',
      nome: 'Tarefa Mock 2',
      dataPrevisaoConclusao: '2021-08-31',
      dataConclusao: '2021-09-01',
    },
  ]);

  const sair = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('usuarioNome');
    localStorage.removeItem('usuarioEmail');
    props.setAccessToken('');
  };

  return (
    <>
      <Header sair={sair} />
      <Filtros />
      <Listagem tarefas={tarefas} />
      <Footer />
    </>
  );
};
