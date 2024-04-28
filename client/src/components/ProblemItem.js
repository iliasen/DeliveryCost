import React, { useContext } from 'react'
import { observer } from 'mobx-react-lite'
import '../styles/OrderItem.css'
import { Context } from '../index'
import ChangeOrderStatus from './modals/ChangeOrderStatus'

const ProblemItem = observer(({ order }) => {
  const { user } = useContext(Context)


  const statusOptions = [
    { value: 'WITHOUT', label: 'Без статуса' },
    { value: 'NEW', label: 'Новый' },
    { value: 'ADOPTED', label: 'Принят' },
    { value: 'DELIVERED', label: 'Доставлен' },
    { value: 'CANCELLED', label: 'Отменён' },
    { value: 'COMPLETE', label: 'Завершен' },
  ]

  return (
    <div className="OrderItem">
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


        <div className="d-flex justify-content-end">
          <strong>Итого:</strong> {order.price} р.
        </div>

      </div>
    </div>
  )
})

export default ProblemItem