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

export const getAverageRating = async (id)=>{
    try {
        const {data} = await $authHost.get("api/rating/average/"+ id);
        return data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const createRating = async (partnerId, rate, feedback) => {
    const {data} = await $authHost.post('api/rating/' + partnerId, {rate, feedback})
    return data
}

export const fetchRating = async (partnerId) => {
    const {data} = await $authHost.get('api/rating/' + partnerId)
    return data
}


export const deleteRating = async(partnerId)=>{
    const {data} = await $authHost.delete('api/rating/' + partnerId)
    return data
}