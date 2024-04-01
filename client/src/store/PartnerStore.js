import { makeAutoObservable } from "mobx";

export default class PartnerStore {
    constructor() {
        this._partners = [];
        this._selectedPartner = {}
        makeAutoObservable(this);
    }



    setPartners(partners){
        this._partners=partners;
    }

    setPartner(partner){
        this._selectedPartner = partner;
    }
    get partners(){
        return this._partners
    }

    get selectedPartner(){
        return this._selectedPartner
    }
}
