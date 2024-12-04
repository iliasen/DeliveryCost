import { $authHost, $host } from './index'
import axios from 'axios'
import { load } from '@2gis/mapgl'

// const key = 'bce1dd40-8c89-4c5d-beea-fce9c1e8071f'

//const key = '061ff499-0e05-4984-b5b5-068b1fe35299' //основной

// const key = 'c104c8d1-9496-401d-9dd8-be12960697e5'

// const key = '1cd344fc-02f9-49ec-8007-56f415a6f886'

const key = '158628dd-3eb7-4612-bbc7-692c1d5db3d8' // cвежий

export const getOrder = async () => {
  const { data } = await $authHost.get('api/order')
  return data
}

export const getWorkOrder = async () => {
  const { data } = await $authHost.get('api/order/partner')
  return data
}

export const getOrdersForDriver = async (id) => {
  const { data } = await $authHost.get('api/order/driver/'+id)
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


export const addOrder = async (id, pointOfDeparture, deliveryPoint, distance, transportType, comment, weight,length, width, height, price) => {
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
      length,
      width
    },
  }
  const { data } = await $authHost.post('/api/order/' + id, order)
  return data
}

export const transferOrdersToDriver = async (id, selectedOrders) => {

  const { data } = await $host.post('api/order/transfer/' + id, {orderList: selectedOrders});
  return data;
};

export const changeStatus = async (id, status) => {
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
