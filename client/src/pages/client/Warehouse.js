import React, { useEffect, useState } from 'react';
import { ProgressBar } from 'react-bootstrap';

import { getSpace } from '../../http/warehouseAPI'

import '../../styles/Warehouse.css'

const Warehouse = () => {
  const [space, setSpace] = useState('');

  useEffect(() => {
    getSpace().then((data) => setSpace(data));
  }, []);

  const calculateProgress = () => {
    // if (!space || !space.totalWeight || !space.freeSpace) {
    //   return 0;
    // }
    // space.freeSpace = 180000
    // const totalWeight = space.totalWeight;
    const totalWeight = 7500;
    const weightCapacity = totalWeight + 180000;
    const progress = (totalWeight / weightCapacity) * 100;
    return Math.round(progress);
  };

  return (
    <div className="warehouse-container">
      <h2 className="warehouse-title">Склад</h2>
      <div className="warehouse-info">
        <p>Свободное место: {space.freeSpace} кг.</p>
        <p>Занято: {7500} кг.</p>
      </div>
      <div className="progress-bar-container">
        <ProgressBar now={calculateProgress()} label={`${calculateProgress()}%`} className="custom-progress-bar" />
      </div>
    </div>
  );
};

export default Warehouse;
