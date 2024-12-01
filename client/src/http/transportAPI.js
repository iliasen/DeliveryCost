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
  }catch(error) {
    console.error(error);
    throw error;
  }
}
