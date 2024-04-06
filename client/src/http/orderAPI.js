import {$authHost} from './index'
import axios from 'axios'

export const addOrder = async (user, address, comment) => {
    const { data } = await $authHost.post('api/order',{user,address, comment})
    return data
}


export const getOrder = async () => {
    const { data } = await $authHost.get('api/order');

    const adaptedOrders = data.map((order) => ({
        id: order.id,
        username: order.username,
        address: order.address,
        comment: order.comment,
        createdAt: order.createdAt,
        updatedAt: order.updatedAt,
        items: order.orderItems.map((orderItem) => ({
            id: orderItem.item.id,
            name: orderItem.item.name,
            price: orderItem.item.price,
            quantity: orderItem.quantity,
        })),
    }));

    return adaptedOrders;
};

export const completeOrder = async (id)=>{
    const { data } = await $authHost.delete('api/order/'+id)
    return data
}

export const getAddress = async (point) =>{
    const response = await axios.get('https://catalog.api.2gis.com/3.0/items/geocode', {
        params: {
            q: point,
            fields: 'items.point',
            key: '061ff499-0e05-4984-b5b5-068b1fe35299'
            // key: '1cd344fc-02f9-49ec-8007-56f415a6f886'
        }
    });

    return response.data;
}