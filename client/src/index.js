import React, { createContext } from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import UserStore from './store/UserStore'
import RatingStore from './store/RatingStore'
import LocationStore from './store/LocationStore'
import { TelNumberStore } from './store/TelNumberStore'
import OrderStore from './store/OrderStore'
import PartnerStore from './store/PartnerStore'
import NotificationStore from './store/NotificationStore'

export const MapContext = React.createContext([undefined, () => {}]);
const MapProvider = (props) => {
  const [mapInstance, setMapInstance] = React.useState();

  return (
    <MapContext.Provider value={[mapInstance, setMapInstance]}>
      {props.children}
    </MapContext.Provider>
  );
};

// Ваш существующий контекст
export const Context = createContext(null);

const root = ReactDOM.createRoot(document.getElementById('root'));

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
      map: MapContext // Используйте новый контекст карты вместо React.createContext([undefined, () => {}])
    }}
  >
    <MapProvider>
      <App />
    </MapProvider>
  </Context.Provider>,
);
