import React, { useEffect, useState } from 'react'
import { load } from '@2gis/mapgl'
import MapWrapper from './MapWrapper'
import { getAddress, TSLRequest } from '../http/orderAPI'
import { MapContext} from '../index'
import axios from 'axios'

const MapBox = ({ selectedOrders }) => {
  const [taskResult, setTaskResult] = useState(null)
  const [_, setMapInstance] = React.useContext(MapContext);
  let map
  useEffect(() => {
    load().then((mapglAPI) => {
      map = new mapglAPI.Map('map_container', {
        center: [37.596713, 55.768474],
        zoom: 5,
        key: 'c104c8d1-9496-401d-9dd8-be12960697e5',
        // key: '0775c594-5d3a-49d2-a4b7-d4b5af596fa1',
      })

    })
    setMapInstance(map);
    return () => map && map.destroy()
  }, [])



  async function getSolution(task_id) {
    const key='061ff499-0e05-4984-b5b5-068b1fe35299'
    // await getSequence('https://disk.2gis.com/prod-navi-vrp-bucket/0846b315772bef1f3f496b38c5ba154b-sln.json');
    try {
      const response = await axios.get(`http://catalog.api.2gis.com/logistics/vrp/1.0/status?key=${key}&task_id=${task_id}`);
      const parsed = response.data;

      if (parsed) {
        if (parsed.status === 'Run') {
          setTimeout(getSolution, 5000, task_id);
        } else if (parsed.status === 'Done' || parsed.status === 'Partial') {
          let url_solution = parsed.urls.url_vrp_solution;
          console.log(url_solution);
          await getSequence(url_solution); // Передача экземпляра карты в функцию getSequence
        }
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }

  async function getSequence(url_solution) {
    try {

      const response = await axios.get(url_solution);
      const parsed = response.data;
      console.log(parsed);
      let sequence = parsed['routes'][0]['points'];

      let i = 0;
      const waypoints = coordinates.then((resolvedCoordinates) => {
        return resolvedCoordinates.flatMap((coordinate, index) => {
          const departureWaypoint = {
            waypoint_id: index,
            point: coordinate.pointOfDeparture,
          }

          const deliveryWaypoint = {
            waypoint_id: index + selectedOrders.length,
            point: coordinate.deliveryPoint,
          }

          return [departureWaypoint, deliveryWaypoint]
        })
      })

      const resolvedWaypoints = await waypoints


      // Добавление точек на карту
      for await (let elem of sequence) {
        const current = resolvedWaypoints[elem];
        console.log(current);
        const currentIndex = i; // Зафиксировать текущее значение i

         load().then((mapglAPI) => {
          const marker = new mapglAPI.Marker(map, {
            coordinates: [
              current.point.lon,
              current.point.lat
            ],
            label: {
              text: 'Точка ' + (currentIndex + 1), // Использовать currentIndex + 1
              offset: [0, -75],
              image: {
                url: 'https://docs.2gis.com/img/mapgl/tooltip.svg',
                size: [100, 40],
                padding: [10, 10, 20, 10],
              },
            },
          });
        }).catch((error) => {
          console.error('Ошибка при создании маркера:', error);
        });

        i = i + 1;
      }
    } catch (error) {
      console.error(error);
    }
  }

  const getCoordinates = async (selectedOrders) => {
    let coordinates =[]
    const getAddressPromises = selectedOrders.map(async (order) => {
      const pointOfDepartureAddressPromise = getAddress(order.route.pointOfDeparture);
      const deliveryPointAddressPromise = getAddress(order.route.deliveryPoint);
      const [pointOfDepartureAddress, deliveryPointAddress] = await Promise.all([
        pointOfDepartureAddressPromise,
        deliveryPointAddressPromise,
      ]);

      const pointOfDepartureCoordinates = pointOfDepartureAddress.result.items[0].point;
      const deliveryPointCoordinates = deliveryPointAddress.result.items[0].point;

      coordinates.push({
        orderId: order.id,
        pointOfDeparture: pointOfDepartureCoordinates,
        deliveryPoint: deliveryPointCoordinates,
      });
    });

    await Promise.all(getAddressPromises);

    return coordinates;
  };

  const coordinates = getCoordinates(selectedOrders)

  console.log(coordinates)

  useEffect(() => {
    const fetchTSLData = async () => {
      if (selectedOrders.length >= 3) {
        //await getSolution(1);
        await new Promise((resolve) => setTimeout(resolve, 3000));
        try {
          const result = await TSLRequest(selectedOrders, coordinates);
          console.log(result);

          const taskId = result.task_id;

          const status = await getSolution(taskId);
          setTaskResult(status);
        } catch (error) {
          console.error(error);
        }
      }
    };

    fetchTSLData().then();
  }, [selectedOrders]);

  console.log(taskResult)

  return (
    <div style={{ width: '100%', height: '100vh' }}>
      <div style={{ width: '100%', height: '100%' }}>
        <MapWrapper />
      </div>
    </div>
  )
}

export default MapBox
