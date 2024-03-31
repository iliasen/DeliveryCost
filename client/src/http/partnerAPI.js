import {$authHost} from './index'

export const getPartners = async () => {
    try {
        const response = await $authHost.get("api/partner/all"); // Отправляем запрос на сервер и получаем ответ
        const data = response.data;
        return data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};