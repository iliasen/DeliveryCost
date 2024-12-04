import { $authHost } from './index'

export const addTransport = async (transportType, tonnage, volume) => {
  try {
    const response = await $authHost.post("api/transport",
      {transportType, tonnage, volume}
    );
    return response.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const getTransports = async () => {
  try {
    const response = await $authHost.get("api/transport");
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === 404) {
      console.error('Транспорты не найдены.');
      alert('Транспорты не найдены.');
    } else {
      console.error(error);
    }
    throw error;
  }
}

export const getTransportsForUser = async (id) => {
  try {
    const response = await $authHost.get("api/transport/"+id);
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === 404) {
      console.error('Транспорты не найдены.');
      alert('Транспорты не найдены.');
    } else {
      console.error(error);
    }
    throw error;
  }
}

export const getTransportByType = async (type) => {
  try {
    const response = await $authHost.get(`api/transport/by_type`,
       type
    );
    return response.data;
  } catch (error) {
    if (error.response && error.response.status === 404) {
      console.error('Транспорты не найдены для данного типа.');
      alert('Транспорты не найдены для данного типа.');
    } else {
      console.error(error);
    }
    throw error;
  }
}
