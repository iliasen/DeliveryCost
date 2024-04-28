import { $authHost } from './index'
import axios from 'axios'
import { load } from '@2gis/mapgl'

// const key = 'bce1dd40-8c89-4c5d-beea-fce9c1e8071f'

const key = '061ff499-0e05-4984-b5b5-068b1fe35299' //основной

// const key = '1cd344fc-02f9-49ec-8007-56f415a6f886'

// const key = '0775c594-5d3a-49d2-a4b7-d4b5af596fa1'

export const getOrder = async () => {
  const { data } = await $authHost.get('api/order')
  console.log(data)
  return data
}

export const completeOrder = async (id) => {
  const { data } = await $authHost.delete('api/order/' + id)
  return data
}

export const getAddress = async (point) => {
  const response = await axios.get('https://catalog.api.2gis.com/3.0/items/geocode', {
    params: {
      q: point,
      fields: 'items.point',
      key: key,

    },
  })

  return response.data
}

export const getDistance = async (points) => {
  const data = {
    points,
    sources: [0],
    targets: [1],
  }

  const config = {
    headers: {
      'Content-Type': 'application/json',
    },
    params: {
      key: key,
      version: '2.0',
    },
  }

  try {
    const response = await axios.post(
      'https://routing.api.2gis.com/get_dist_matrix',
      data,
      config,
    )

    return response.data

  } catch (error) {
    throw error
  }
}

export const TSLRequest = async (selectedOrders, coordinates) => {
  const agents = [
    {
      'agent_id': 0,
      'start_waypoint_id': 0,
    },
  ]

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

  try {
    const resolvedWaypoints = await waypoints
    const data = {
      agents,
      waypoints: resolvedWaypoints,
    }

    const config = {
      headers: {
        'Content-Type': 'application/json',
      },
      params: {
        key: key,
      },
    }

    const response = await axios.post(
      'https://routing.api.2gis.com/logistics/vrp/1.1.0/create',
      data,
      config,
    )
    console.log(data)
    return response.data
  } catch (error) {
    throw error
  }
}

export const getSolution = async (task_id) => {
  try {
    const response = await axios.get(`http://catalog.api.2gis.com/logistics/vrp/1.0/status?key=${key}&task_id=${task_id}`)
    const parsed = response.data

    if (parsed) {
      if (parsed.status === 'Run') {
        setTimeout(getSolution, 5000, task_id)
      } else if (parsed.status === 'Done' || parsed.status === 'Partial') {
        let url_solution = parsed.urls.url_vrp_solution
        console.log(url_solution)
        await getSequence(url_solution)
      }
    }
  } catch (error) {
    console.error('Error:', error)
  }
}

async function getSequence(url_solution) {
  try {
    const response = await axios.get(url_solution)
    const parsed = response.data
    console.log(parsed)
    let sequence = parsed['routes'][0]['points']

    let i = 0

    // Добавление точек на карту
    load().then((mapglAPI) => {
      const map = new mapglAPI.Map('map_container', {
        center: [37.596713, 55.768474],
        zoom: 13,
        key: key,
      })

      const marker = new mapglAPI.Marker(map, {
        coordinates: [37.596713, 55.768474],
      });
      return () => map && map.destroy()
    //   for (let elem of sequence) {
    //     i = i + 1
    //     const marker = new mapglAPI.Marker(map, {
    //       coordinates: [
    //         elem['point']['lon'],
    //         elem['point']['lat'],
    //       ],
    //       label: {
    //         text: 'Точка ' + i,
    //         offset: [0, -75],
    //         image: {
    //           url: 'https://docs.2gis.com/img/mapgl/tooltip.svg',
    //           size: [100, 40],
    //           padding: [10, 10, 20, 10],
    //         },
    //       },
    //     })
    //   }
    })
  } catch (error) {
    // Обработка ошибки
    console.error(error)
  }
}


// export const getTaskStatus = async (taskId) => {
//     const url = `https://routing.api.2gis.com/logistics/vrp/1.1.0/status`;
//     const params = {
//         task_id: taskId,
//         key: key,
//     };
//
//     try {
//         const response = await axios.get(url, { params });
//         const status = response.data.status;
//
//         if (status === 'Partial' || status === 'Done') {
//             if (response.data.urls && response.data.urls.url_vrp_solution) {
//                 const vrpSolutionUrl = response.data.urls.url_vrp_solution;
//                 const vrpSolutionResponse = await axios.get(vrpSolutionUrl);
//                 const vrpSolutionData = vrpSolutionResponse.data;
//                 // Дальнейшая обработка данных из vrpSolutionData
//                 console.log(vrpSolutionData);
//                 return vrpSolutionData;
//             }
//         }
//
//         console.log(status);
//         return status;
//     } catch (error) {
//         // Обработка ошибки
//         console.error(error);
//         throw error;
//     }
// };

export const addOrder = async (id, pointOfDeparture, deliveryPoint, distance, transportType, comment, weight, price) => {
  console.log(transportType)
  const order = {
    order: {
      comment,
      price,
      route: {
        pointOfDeparture,
        deliveryPoint,
        distance,
        deliveryTime: '2022-12-31T10:30:00',
        transportType,
      },
    },

    cargo: {
      weight,
    },
  }
  const { data } = await $authHost.post('/api/order/' + id, order)
  return data
}

export const changeStatus = async (id, status) => {
  // const { data } = await $authHost.put(`/api/order/status/${id}`, {status:status});
  const { data } = await $authHost.put(`/api/order/status/${id}`, null, {
    params: { status },
  })
  return data
}

export const resolveProblem = async (maxWeight) => {
  const { data } = await $authHost.post('api/order/back_problem', { maxWeight },
  )
  console.log(data)
  return data
}