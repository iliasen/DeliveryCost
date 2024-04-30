import { $authHost, $host } from './index'

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

export const checkFiends = async ()=>{
    const {data} = await $authHost.get('api/partner/fields_check');
    return data
}

export const updateFields = async(fields)=>{
    const {data} = await $authHost.put('api/partner/update_fields', fields)
    return data
}

export const fetchLogoImage = async (imageName)=>{
    const {data} = await $host.get('api/partner/img/'+imageName, {
        responseType: 'blob',
    })
    return data
}