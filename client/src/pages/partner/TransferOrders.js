import React, { useContext, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';
import { getOrdersForDriver, transferOrdersToDriver } from '../../http/orderAPI'

const TransferOrders = () => {
  const { driverId } = useParams();
  const [orders, setOrders] = useState([]);
  const [selectedOrders, setSelectedOrders] = useState([]);

  useEffect(()=> {
    getOrdersForDriver(driverId).then((orders)=> setOrders(orders))
  },[])

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
      You are here in transfer order page for driver ID: {driverId}
      <div className="">
        {orders.length !== 0 ? (
          <div>
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
            <button onClick={() => sentOrdersToDriver(driverId, selectedOrders)}>Назначить заказы</button>

          </div>
        ) : (
          <div>Подходящих заказов для данного водителя, нет</div>
        )}
      </div>
    </div>
  );
}

export default TransferOrders;
