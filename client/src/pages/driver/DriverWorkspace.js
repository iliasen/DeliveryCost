import React, { useEffect, useState } from 'react';
import { getAddress, getOrder } from '../../http/orderAPI';
import MapWrapper from '../../components/MapWrapper';
import { MapContext } from '../../index';
import { load } from '@2gis/mapgl';
import { Directions } from '@2gis/mapgl-directions';

const DriverWorkspace = () => {
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [_, setMapInstance] = React.useContext(MapContext);
  const [map, setMap] = useState(null);
  const [directions, setDirections] = useState(null);

  useEffect(() => {
    load().then((mapglAPI) => {
      const mapInstance = new mapglAPI.Map('map_container', {
        center: [37.596713, 55.768474],
        zoom: 5,
        key: '158628dd-3eb7-4612-bbc7-692c1d5db3d8'
      });
      const directionsInstance = new Directions(mapInstance,
        {directionsApiKey: '158628dd-3eb7-4612-bbc7-692c1d5db3d8'})
      setMap(mapInstance);
      setDirections(directionsInstance);
      setMapInstance(mapInstance);
    });

    return () => {
      if (map) {
        map.destroy();
      }
    };
  }, []);

  useEffect(() => {
    getOrder().then((orders) => setOrders(orders));
  }, []);

  const getCoordinates = async (selectedOrder) => {
    if (selectedOrder && selectedOrder.route) {
      const pointOfDepartureAddressPromise = getAddress(selectedOrder.route.pointOfDeparture);
      const deliveryPointAddressPromise = getAddress(selectedOrder.route.deliveryPoint);

      const [pointOfDepartureAddress, deliveryPointAddress] = await Promise.all([
        pointOfDepartureAddressPromise,
        deliveryPointAddressPromise,
      ]);

      const pointOfDepartureCoordinates = pointOfDepartureAddress.result.items[0].point;
      const deliveryPointCoordinates = deliveryPointAddress.result.items[0].point;

      return {
        orderId: selectedOrder.id,
        pointOfDeparture: pointOfDepartureCoordinates,
        deliveryPoint: deliveryPointCoordinates,
      };
    }
    return null;
  };

  useEffect(() => {
    const fetchCoordinates = async () => {
      if (selectedOrder) {
        const coords = await getCoordinates(selectedOrder);
        console.log(coords);
        if (coords && directions && map) {
          // Очистка предыдущего маршрута, если он есть
          directions.clear();

          // Прокладываем маршрут
          directions.carRoute({
            type: 'car', // Укажите тип маршрута
            point_a_name: 'Source', // Название точки отправления
            point_b_name: 'Target', // Название точки доставки
            locale: 'en', // Укажите локализацию
            points: [
              [coords.pointOfDeparture.lon, coords.pointOfDeparture.lat], // Координаты отправления
              [coords.deliveryPoint.lon, coords.deliveryPoint.lat], // Координаты доставки
            ],
          });
        }
      }
    };

    fetchCoordinates();
  }, [selectedOrder, directions, map]); // добавляем зависимости


  return (
    <div className="partner-workspace">
      <div className="order-list">
        <h4>Расчет маршрута</h4>
        <div>
          {orders.length !== 0 ? (
            <div>
              {orders.map((order) => (
                <div
                  key={order.id}
                  className={`order-item ${selectedOrder === order ? 'selected' : ''}`}
                  onClick={() => setSelectedOrder(order)}
                >
                  <div className="order-details">
                    <input
                      type="checkbox"
                      checked={selectedOrder === order}
                      onChange={() => setSelectedOrder(order)}
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
      <div style={{ width: '100%', height: '100vh' }} className="map_container">
        <div style={{ width: '100%', height: '100%' }}>
          <MapWrapper />
        </div>
      </div>
    </div>
  );
};

export default DriverWorkspace;
