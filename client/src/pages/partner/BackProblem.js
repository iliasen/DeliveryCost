import React, { useContext, useEffect, useState } from 'react'
import { resolveProblem } from '../../http/orderAPI'
import { Context } from '../../index'
import ProblemItem from '../../components/ProblemItem'
import { Button } from 'react-bootstrap'

import '../../styles/BackProblem.css'

const BackProblem = () => {
  const [weight, setWeight] = useState(0);
  const [showResetButton, setShowResetButton] = useState(false);
  const { order } = useContext(Context);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (weight !== 0) {
      resolveProblem(weight).then((orders) => {
        order.setOrder(orders);
        setShowResetButton(true);
      });
    }
  };

  const handleReset = () => {
    setWeight(0);
    order.setOrder([]);
    setShowResetButton(false);
  };

  return (
    <div className="back-problem-container">
      {order.order.length !== 0 ? (
        <div>
          {showResetButton && <Button  className='m-3 btn btn-dark' onClick={handleReset}>Сбросить</Button>}
          {order.order.map((order) => (
            <ProblemItem key={order.id} order={order} />
          ))}
        </div>
      ) : (
        <div className="waitingWeight">
          <form className="back-problem-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="weight" className='weight_label'>Введите вес груза (кг.), по которому будут подобраны подходящие заказы</label>
              <input
                className="weight-input"
                id="weight"
                name="weight"
                type="number"
                required
                value={weight}
                onChange={(e) => setWeight(e.target.value)}
              />
            </div>
            <Button className="submit-button" type="submit">Отправить</Button>
          </form>
        </div>
      )}
    </div>
  );
};

export default BackProblem;
