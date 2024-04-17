import { makeAutoObservable, toJS } from 'mobx'

export default class OrderStore {
    constructor() {
        this._orders = [];
        makeAutoObservable(this);
    }

    setOrder(order) {
        this._orders = order.map(item => toJS(item));
        console.log(this._orders)
    }
    get order(){
        return this._orders
    }
}
