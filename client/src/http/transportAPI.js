import { $authHost} from './index'

export const addTransport = async (transportType, tonnage, volume) => {
  try {
    const response = await $authHost.post("api/transport",
      {transportType, tonnage, volume}
    );
    const data = response.data;
    return data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};
