import React from 'react';
import { Header } from '../assets/componentes/Header';
import { Filtros } from '../assets/componentes/Filtros';

export const Home = props => {
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
    </>
  );
};
