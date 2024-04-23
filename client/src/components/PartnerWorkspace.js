import React, { useEffect } from 'react';
import { load } from '@2gis/mapgl';
import MapWrapper from './MapWrapper'

import '../styles/PartnerWorkspace.css';
import { observer } from 'mobx-react-lite'

const PartnerWorkspace = observer(() => {

  useEffect(() => {
    let map;
    load('https://mapgl.2gis.com/api/js/v1.4.2').then((mapglAPI) => {
      map = new mapglAPI.Map('map_container', {
        center: [33.902878, 27.561039],
        zoom: 8,
        // key: '061ff499-0e05-4984-b5b5-068b1fe35299',
      });


      const marker = new mapglAPI.Marker(map, {
        coordinates: [53.902878, 27.561039]
      })

      const marker2 = new mapglAPI.Marker(map, {
        coordinates: [55.31878, 25.23584],
      });
    });

    return () => map && map.destroy();
  }, []);

  return (
    <div style={{ width: '100vw', height: '100vh' }} className='d-flex gap-2'>
       <div>
        <h4>Расчет заказов</h4>
         <div>

         </div>
       </div>
      <div style={{ width: '100%', height: '100%' }}>
        <MapWrapper />
      </div>
    </div>
  );
})

export default PartnerWorkspace;