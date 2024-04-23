import React, { useContext, useEffect, useState } from 'react';
import { delNotifications, getNotifications, viewNotify } from '../http/notificationAPI'
import { Context } from '../index';
import { observer } from 'mobx-react-lite';


import '../styles/Notify.css';
import { Button } from 'react-bootstrap'

const Notifications = observer(() => {
  const {user, notification } = useContext(Context);
  const [showNotifications, setShowNotifications] = useState(false);

  useEffect(() => {
    getNotifications().then((notifications) =>
      notification.setNotifications(notifications)
    );
  }, [notification]);

  const handleButtonClick = () => {
    setShowNotifications(!showNotifications);
  };

  const delNotifies = () =>{
    delNotifications().then((e) => {getNotifications().then((notifications) =>
      notification.setNotifications(notifications)
    )})
  }

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

  const handleNotificationMouseEnter = (id) => {//увидел ли пользователь уведомление
    viewNotify(id).then()
    console.log('Уведомление наведено на:', id);
  };

  const changeTime = (notification) => {
    const changeTime = new Date(notification);
    const currentTime = new Date();

    // Проверяем, является ли дата сегодняшней
    const isToday = (
      changeTime.getDate() === currentTime.getDate() &&
      changeTime.getMonth() === currentTime.getMonth() &&
      changeTime.getFullYear() === currentTime.getFullYear()
    );

    let formattedTime;

    if (isToday) {
      // Если дата сегодняшняя, то отображаем только время
      formattedTime = changeTime.toLocaleTimeString('ru-RU', {
        hour: '2-digit',
        minute: '2-digit',
      });
    } else {
      // Если дата не сегодняшняя, то отображаем полную дату и время
      formattedTime = changeTime.toLocaleString('ru-RU', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    }

    return formattedTime;
  };

  const unreadNotificationCount = () => {
    if (user.user.role === 'PARTNER') {
      return notification.notifications.some((notify) => !notify.partnerChecked);
    } else if (user.user.role === 'CLIENT') {
      return notification.notifications.some((notify) => !notify.clientChecked);
    }

    return false;
  };

  return (
    <div>
      <div onClick={handleButtonClick} style={{ marginLeft: 12, marginRight: 12 }} className='d-flex'>
        {showNotifications ? <div className="notifyOn" /> : <div className="notifyOff" />}
        {unreadNotificationCount() && <div className='notifyIndicator'/>}
      </div>

      {showNotifications && (
        <div className="notificationContainer">
          {notification.notifications.length !== 0 ? (
            <div className="notificationList">
              {notification.notifications
                .slice()
                .reverse()
                .map((notify) => (
                  <div key={notify.id} className={`notificationItem ${
                    (notify.partnerChecked && user.user.role === 'PARTNER') ||
                    (notify.clientChecked && user.user.role === 'CLIENT')
                      ? 'checkedStyle'
                      : ''
                  }`}
                       onMouseEnter={handleNotificationMouseEnter(notify.id)}>
                    <div>{changeTime(notify.changeTime)}</div>
                    Заказ № {notify.order.id} Статус: {getStatusLabel(notify.newStatus)}

                  </div>
                ))}
              <div className="d-flex justify-content-end">
                <Button onClick={() => delNotifies()}>Удалить уведомления</Button>
              </div>
            </div>
          ) : (
            <div className="notificationEmpty">Нет уведомлений</div>
          )}
        </div>
      )}
    </div>
  );
});

export default Notifications;