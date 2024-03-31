import { makeAutoObservable } from "mobx";

export default class PartnerStore {
    constructor() {
        this._partners = [];
        makeAutoObservable(this);
    }

    setPartners(partners){
        this._partners=partners;
    }
    get partners(){
        return this._partners
    }
}
