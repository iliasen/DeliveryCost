import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';
import { getDriverOrders, getOrdersForDriver, transferOrdersToDriver } from '../../http/orderAPI'

const TransferOrders = () => {
  const { driverId } = useParams();
  const [driverOrders, setDriverOrders] = useState([]);
  const [orders, setOrders] = useState([]);
  const [selectedOrders, setSelectedOrders] = useState([]);

  useEffect(()=> {
    getOrdersForDriver(driverId).then((orders)=> setOrders(orders))
  },[])

  useEffect(() => {
    getDriverOrders(driverId).then((driverOrders) => setDriverOrders(driverOrders))
  }, [])
  console.log(driverOrders)

  console.log(selectedOrders)


  const handleOrderClick = (order) => {
    if (selectedOrders.includes(order)) {
      setSelectedOrders(selectedOrders.filter((selectedOrder) => selectedOrder !== order));
    } else {
      setSelectedOrders([...selectedOrders, order]);
    }
  };

  const sentOrdersToDriver = async (driverId, orders) => {
    transferOrdersToDriver(driverId, orders).then(()=>(alert("Товары успешно назначены")))
  }

  return (
    <div className="container p-5" style={{height:'90vh'}}>
      Вы на странице взаимодействия с водителем по номером: {driverId}

      {driverOrders.length !== 0 ? <div>
          <h4>Заказы которыми уже есть у водителя:</h4>
          <div className="d-flex">
            {driverOrders.map((order) => {
              {console.log(order);}
              return (<div className="order-info" key={order.id}>
                <div>Заказ #{order.id}</div>
                <div>Точка отправления: {order.route.pointOfDeparture}</div>
                <div>Точка доставки: {order.route.deliveryPoint}</div>
              </div>)
            })}
          </div>
        </div>
        : <h4>У водителя нет заказов</h4>}

      <div className="mt-5">
        {orders.length !== 0 ? (
          <div>
            <h4>Выберите заказы чтобы назначить их водителю</h4>
            {orders.map((order) => (
              <div
                key={order.id}
                className={`order-item ${selectedOrders.includes(order) ? 'selected' : ''}`}
                onClick={() => handleOrderClick(order)}
              >
                <div className="order-details">
                  <input
                    type="checkbox"
                    checked={selectedOrders.includes(order)}
                    onChange={() => handleOrderClick(order)}
                  />
                  <div className="order-info">
                    <div>Заказ #{order.id}</div>
                    <div>Тип трансорта: {order.route.transportType}</div>
                    <div>Точка отправления: {order.route.pointOfDeparture}</div>
                    <div>Точка доставки: {order.route.deliveryPoint}</div>
                  </div>
                </div>
              </div>
            ))}
            <button className="orderSentButton mt-5" onClick={() => sentOrdersToDriver(driverId, selectedOrders)}>Назначить заказы</button>

          </div>
        ) : (
          <div>Подходящих заказов для данного водителя, нет</div>
        )}
      </div>
    </div>
  );
}

export default TransferOrders;
