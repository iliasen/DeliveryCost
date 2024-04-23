import React, { createContext } from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import UserStore from './store/UserStore'
import RatingStore from "./store/RatingStore";
import LocationStore from "./store/LocationStore";
import {TelNumberStore} from "./store/TelNumberStore";
import OrderStore from "./store/OrderStore";
import PartnerStore from "./store/PartnerStore";
import NotificationStore from './store/NotificationStore'

export const Context = createContext(null)

const root = ReactDOM.createRoot(document.getElementById('root'))


root.render(
  <Context.Provider
    value={{
      user: new UserStore(),
      partners: new PartnerStore(),
      rating: new RatingStore(),
      notification: new NotificationStore(),
      order: new OrderStore(),
      location: new LocationStore(),
      number: new TelNumberStore(),
    }}
  >
    <App />
  </Context.Provider>
)
