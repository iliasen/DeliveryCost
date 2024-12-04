import { $authHost } from './index'

export const getDrivers = async () => {
  try {
    const response = await $authHost.get("api/driver/all");
    return response.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const getFreeDrivers = async () => {
  try {
    const response = await $authHost.get("api/driver/free");
    return response.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const addTransport = async (driver, transport) => {
  try {
    const response = await $authHost.put(`api/driver/${driver.id}`, transport);
    return response.data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};
