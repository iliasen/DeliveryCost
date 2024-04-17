import React, { useContext, useState } from 'react'
import {observer} from "mobx-react-lite";
import '../../styles/OrderItem.css';
import {completeOrder} from "../../http/orderAPI";
import { Context } from '../../index'
import button from 'bootstrap/js/src/button'
import Modal from 'react-bootstrap/Modal'
import ChangeOrderStatus from './ChangeOrderStatus'

const OrderItem =observer(  ({ order }) => {
    const {user} = useContext(Context)
    const [showModal, setShowModal] = useState(false);
    const [orderId, setOrderId] = useState(0)
    const statusOptions = [
      { value: 'WITHOUT', label: 'Без статуса' },
      { value: 'NEW', label: 'Новый' },
      { value: 'ADOPTED', label: 'Принят' },
      { value: 'DELIVERED', label: 'Доставлен' },
      { value: 'CANCELLED', label: 'Отменён' },
      { value: 'COMPLETE', label: 'Завершен' }
    ];

    const getStatusLabel = (status) => {
      const foundStatus = statusOptions.find((option) => option.value === status);
      return foundStatus ? foundStatus.label : '';
    };
    const completed = ()=>{
        completeOrder(order.id).then()
        window.location.reload()
    }
    return (
      <div className='OrderItem'>
        <div>
          <strong>Заказ</strong> №{order.id}
        </div>

        <div className='d-flex justify-content-between align-items-center'>
          <div>
            Поставщик: {order.partner.companyName}
            <p>Тип транспорта: {order.route.transportType}</p>
            <p>{order.comment && <div>Комментарий к заказу: {order.comment}</div>}</p>
          </div>
          <div>
            {user.user.role === 'PARTNER' && (
              <div className='changeOrderStatus' onClick={() => {
                setShowModal(true)
                setOrderId(order.id)
              }}>
                Изменить статус заказа
              </div>
            )}
            <h4 className='text-center'>{getStatusLabel(order.orderStatus)}</h4>
            <button className="completeOrder" onClick={completed}>
              Заказ выполнен
            </button>
          </div>

        </div>

        {user.user.role === "PARTNER" &&
          <p>
            Информация о клиенте:
            <div>Имя: {order.client.firstName}</div>
            <div>Фамилия: {order.client.lastName}</div>
            <div>Email: {order.client.email}</div>
            <div>Телефон: {order.client.phone}</div>
          </p>
        }

        <div>
          Информация о доставке:
          <div>Точка отправления: {order.route.pointOfDeparture}</div>
          <div>Точка доставки: {order.route.deliveryPoint}</div>
          <div>Время доставки: {order.route.deliveryTime}</div>
          <div>Расстояние: {order.route.distance} м.</div>
        </div>

        <div>
          Вес груза: {order.cargo.weight} кг.
        </div>

        <div className="d-flex justify-content-end">
          <strong>Итогоㅤ</strong>00 р.
        </div>

        <ChangeOrderStatus show={showModal} onHide={() => setShowModal(false)} orderId={orderId}/>
      </div>
    );
})

export default OrderItem;