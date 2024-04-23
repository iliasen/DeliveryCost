import React, { useContext, useState } from 'react'
import {observer} from "mobx-react-lite";
import '../styles/OrderItem.css';
import {completeOrder} from "../http/orderAPI";
import { Context } from '../index'
import ChangeOrderStatus from './modals/ChangeOrderStatus'
import { changeSubscribe } from '../http/notificationAPI'

const OrderItem =observer(  ({ order }) => {
    const {user} = useContext(Context)
    const [showModal, setShowModal] = useState(false);
    const [isSubscribed, setIsSubscribed] = useState(order.clientSubscribe);


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

    const handleToggle = () => {
      setIsSubscribed(!isSubscribed);
      changeSubscribe(order.id, isSubscribed).then()
    };

    return (
      <div className='OrderItem'>
        <div>
          <strong>Заказ</strong> №{order.id}
        </div>

        <div className="d-flex justify-content-between">
          <div>
            <div>
              Поставщик: {order.partner.companyName}
              <p>Тип транспорта: {order.route.transportType}</p>
              <p>{order.comment && <div>Комментарий к заказу: {order.comment}</div>}</p>
            </div>
            {user.user.role === 'PARTNER' &&
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

            {order.cargo &&
              <div>
                Вес груза: {order.cargo.weight} кг.
              </div>
            }
          </div>

          {user.user.role === 'CLIENT' &&
          <div className='d-flex align-items-center'>
            <button onClick={handleToggle} className="completeOrder">
              {isSubscribed ? 'Отписаться от уведомлений' : 'Подписаться на уведомления'}
            </button>
          </div>
          }


          <div className='d-flex flex-column justify-content-between'>
            <div>
              {user.user.role === 'PARTNER' && (
                <div className="changeOrderStatus" onClick={() => {
                  setShowModal(true)
                }}>
                  Изменить статус заказа
                </div>
              )}
              <h4 >{getStatusLabel(order.orderStatus)}</h4>
              <button className="completeOrder" onClick={completed}>
                Заказ выполнен
              </button>
            </div>
            <div className="d-flex justify-content-end">
              <strong>Итогоㅤ</strong>00 р.
            </div>
          </div>


        </div>


        <ChangeOrderStatus show={showModal} onHide={() => setShowModal(false)} orderId={order.id} />
      </div>
    );
})

export default OrderItem;