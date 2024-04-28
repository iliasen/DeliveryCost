import React, { useEffect, useState } from 'react'
import axios from 'axios'
import {load} from '@2gis/mapgl'
// import Toast from '../../../../../Загрузки/tsp_api/toast.min.css'

const Test = () => {
  const [addressArray, setAddressArray] = useState('');
  const [addressSource, setAddressSource] = useState('');
  const [map, setMap] = useState(null);

  useEffect(() => {
    let mapInstance;

    const initializeMap = async () => {
      const mapglAPI = await loadMapGL();
      mapInstance = new mapglAPI.Map('map_container', {
        center: [37.596713, 55.768474],
        zoom: 13,
        key: 'bce1dd40-8c89-4c5d-beea-fce9c1e8071f',
      });
      setMap(mapInstance);
    };

    initializeMap();

    return () => {
      if (mapInstance) {
        mapInstance.destroy();
      }
    };
  }, []);

  const loadMapGL = () => {
    return new Promise((resolve) => {
      const script = document.createElement('script');
      script.src = 'https://mapgl.2gis.com/api/js/v1.4.2';

      script.onload = () => {
        resolve(window.mapgl);
      };

      document.head.appendChild(script);
    });
  };

  const handleAddressArrayChange = (event) => {
    setAddressArray(event.target.value);
  };

  const handleAddressSourceChange = (event) => {
    setAddressSource(event.target.value);
  };

  const calcRoute = async () => {
    try {
      showToast('Данные отправлены. Ожидайте.');

      const addresses = addressArray.split('\n');
      const waypoints = [];
      let agentPoints = '';

      for (let i = 0; i < addresses.length; i++) {
        const address = cleanDoubleSpaces(addresses[i]);
        if (address.length > 5) {
          const coordinates = await getCoordinates(address);
          waypoints.push({
            waypoint_id: i,
            point: {
              lat: coordinates.lat,
              lon: coordinates.lon
            }
          });
        }
      }

      const sourceCoordinates = await getCoordinates(addressSource);
      waypoints.push({
        waypoint_id: waypoints.length,
        point: {
          lat: sourceCoordinates.lat,
          lon: sourceCoordinates.lon
        }
      });

      agentPoints = `"agents": [{"agent_id": 0, "start_waypoint_id": ${waypoints.length - 1}}]`;

      createRoute(waypoints, agentPoints);
    } catch (error) {
      console.error(error);
    }
  };

  const getCoordinates = async (address) => {
    try {
      const response = await axios.get(`https://catalog.api.2gis.com/3.0/items/geocode?q=${address}&fields=items.point&key=1cd344fc-02f9-49ec-8007-56f415a6f886&page_size=1`);
      const { address_name, point } = response.data.result.items[0];
      return { address: address_name, lat: point.lat, lon: point.lon };
    } catch (error) {
      console.error(error);
    }
  };

  const createRoute = async (waypoints, agentPoints) => {
    try {
      const currentTime = new Date().toISOString();
      const requestBody = {
        waypoints,
        agentPoints,
        start_time: currentTime,
        mode: 'driving',
        type: 'jam'
      };

      const response = await axios.post('https://catalog.api.2gis.com/logistics/vrp/1.0/create?key=1cd344fc-02f9-49ec-8007-56f415a6f886', requestBody);
      const { task_id } = response.data;
      getSolution(task_id);
    } catch (error) {
      console.error(error);
    }
  };

  const getSolution = async (taskId) => {
    try {
      const response = await axios.get(`http://catalog.api.2gis.com/logistics/vrp/1.0/status?key=1cd344fc-02f9-49ec-8007-56f415a6f886&task_id=${taskId}`);
      const { status, urls } = response.data;

      if (status === 'Run') {
        setTimeout(() => getSolution(taskId), 5000);
      } else if (status === 'Done') {
        const urlSolution = urls.url_vrp_solution;
        getSequence(urlSolution);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const getSequence = async (urlSolution) => {
    try {
      const response = await axios.get(urlSolution);
      const { routes } = response.data;
      const sequence = routes[0].points;

      const obj = JSON.parse(`{ "waypoints": ${JSON.stringify(sequence)} }`);
      const globalArray = [];

      for (let i = 0; i < sequence.length; i++) {
        const index = sequence[i];
        const { address, lat, lon } = globalArray[index];

        const marker = new load().then(mapglAPI =>{mapglAPI.Marker(map, {
          coordinates: [obj.waypoints[index].point.lon, obj.waypoints[index].point.lat],
          label: {
            text: `Точка ${i + 1}`,
            offset: [0, -75],
            image: {
              url: 'https://docs.2gis.com/img/mapgl/tooltip.svg',
              size: [100, 40],
              padding: [10, 10, 20, 10]
            }
          }
        });})

        mapInfo(marker, address);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const mapInfo = (marker, address) => {
    marker.addEventListener('click', () => {
      showToast(address);
    });
  };

  const showToast = (message) => {
    // Implement your toast notification logic here
    console.log(message);
  };

  const cleanDoubleSpaces = (string) => {
    return string.replace(/\s+/g, ' ').trim();
  };

  return (
    <div>
      <textarea
        value={addressArray}
        onChange={handleAddressArrayChange}
        placeholder="Введите адреса через новую строку"
      ></textarea>
      <input
        value={addressSource}
        onChange={handleAddressSourceChange}
        placeholder="Введите адрес источника"
      />
      <button onClick={calcRoute}>Рассчитать маршрут</button>
    </div>
  );
};
export default Test