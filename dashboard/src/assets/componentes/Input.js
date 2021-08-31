import React from 'react';

export const Input = props => {
  const {
    srcImg,
    altImg,
    inputType,
    inputName,
    value,
    inputPlaceholder,
    setValue,
  } = props;
  return (
    <div className="input">
      <img src={srcImg} alt={altImg} />
      <input
        type={inputType}
        name={inputName}
        placeholder={inputPlaceholder}
        value={value}
        onChange={evt => setValue(evt.target.value)}
      />
    </div>
  );
};
