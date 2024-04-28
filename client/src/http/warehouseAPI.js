import {$authHost} from './index'

export const getSpace = async () => {
    const { data } = await $authHost.get('api/warehouse')
    return data
}
