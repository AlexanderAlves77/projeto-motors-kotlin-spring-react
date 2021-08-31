import React from 'react';
import filtro from '../icones/filtro.svg';

export const Filtros = () => {
  return (
    <div className="container-filtros">
      <div className="title">
        <span>Tarefas</span>
        <img src={filtro} alt="Filtrar tarefas" />
        <div className="form">
          <div>
            <label>Data prevista de conclusão de:</label>
            <input type="date" />
          </div>
          <div>
            <label>até:</label>
            <input type="date" />
          </div>
          <div className="line" />

          <div>
            <label>Status:</label>
            <select>
              <option value={0}>Tudas</option>
              <option value={1}>Ativas</option>
              <option value={2}>Concluídas</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  );
};
