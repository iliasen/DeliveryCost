import {$authHost} from './index'

export const getPartners = async () => {
    try {
        const response = await $authHost.get("api/partner/all");
        const data = response.data;
        return data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};