import React, { useEffect, useState } from 'react';
import { ProgressBar } from 'react-bootstrap';

import { getSpace } from '../http/warehouseAPI'

import '../styles/Warehouse.css'

const Warehouse = () => {
  const [space, setSpace] = useState('');

  useEffect(() => {
    getSpace().then((data) => setSpace(data));
  }, []);

  const calculateProgress = () => {
    if (!space || !space.totalWeight || !space.freeSpace) {
      return 0;
    }

    const totalWeight = space.totalWeight;
    const weightCapacity = space.totalWeight + space.freeSpace;
    const progress = (totalWeight / weightCapacity) * 100;
    return Math.round(progress);
  };

  return (
    <div className="warehouse-container">
      <h2 className="warehouse-title">Склад</h2>
      <div className="warehouse-info">
        <p>Свободное место: {space.freeSpace} кг.</p>
        <p>Занято: {space.totalWeight} кг.</p>
      </div>
      <div className="progress-bar-container">
        <ProgressBar now={calculateProgress()} label={`${calculateProgress()}%`} className="custom-progress-bar" />
      </div>
    </div>
  );
};

export default Warehouse;