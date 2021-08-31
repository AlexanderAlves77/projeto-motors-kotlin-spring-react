import React from 'react';
import adicionar from '../icones/adicionar.svg';

export const Footer = () => {
  return (
    <div className="container-footer">
      <button>
        <img src={adicionar} alt="Adicionar uma tarefa" />
        Adicionar tarefa
      </button>
      <span>
        © Copyright {new Date().getFullYear()} Devaria. Todos os direitos
        reservados.
      </span>
    </div>
  );
};
