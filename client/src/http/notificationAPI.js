import { $authHost } from './index'

export const getNotifications = async ()=> {
  const {data} = await $authHost.get('/api/notify')
  return data
}

export const viewNotify = async(id)=>{
  const {data} = await $authHost.put(`/api/notify/view/${id}`)
  return data
}

export const changeSubscribe = async (id, subscribe) => {
  const { data } = await $authHost.put(`/api/notify/subscribe/${id}?subscribe=${subscribe}`);
  return data;
};

export const delNotifications = async ()=>{
  const {data} = await $authHost.delete('/api/notify')
  return data
}