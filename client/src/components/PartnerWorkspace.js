import React, { useContext, useEffect, useState } from 'react'
import { observer } from 'mobx-react-lite'
import { getOrder } from '../http/orderAPI'
import { Context } from '../index'

import MapBox from './MapBox'

import '../styles/PartnerWorkspace.css'


const PartnerWorkspace = observer(() => {
  const {order} = useContext(Context)
  const [selectedOrders, setSelectedOrders] = useState([]);

  useEffect(()=> {
    getOrder().then((orders)=> order.setOrder(orders))
  },[order])

  const handleOrderClick = (order) => {
    if (selectedOrders.includes(order)) {
      setSelectedOrders(selectedOrders.filter((selectedOrder) => selectedOrder !== order));
    } else {
      setSelectedOrders([...selectedOrders, order]);
    }
  };

  return (
    <div className="partner-workspace">
      <div className="order-list">
        <h4>Расчет заказов</h4>
        <div>
          {order.order.length !== 0 ? (
            <div>
              {order.order.map((order) => (
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
            </div>
          ) : (
            <div>У вас нет заказов</div>
          )}
        </div>
      </div>
      <div className="map-container">
        <MapBox selectedOrders={selectedOrders} />
      </div>
    </div>
  );
});

export default PartnerWorkspace;