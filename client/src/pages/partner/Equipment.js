import React, { useEffect, useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { ADD_TRANSPORT_ROUTE, REGISTRATION_CLIENT_ROUTE } from '../../utils/consts'
import { getTransports } from '../../http/transportAPI'
import { getDrivers } from '../../http/driverAPI'
import { Image } from 'react-bootstrap'
import "../../styles/PartnerEquipment.css"
import AddDriver from '../../components/modals/AddDriver'

const Equipment = () => {
  const [transports, setTransports] = useState([])
  const [drivers, setDrivers] = useState([])
  const [visible, setVisible] = useState(false)
  const [selectedTransport, setSelectedTransport] = useState(null);
  let nav= useNavigate()

  const getIconForTransportType = (type) => {
    switch (type) {
      case 'CAR':
        return <img width="100" height="100" src="https://img.icons8.com/papercut/120/car.png" alt="car" />
      case 'TRUCK':
        return <img width="100" height="100" src="https://img.icons8.com/papercut/120/truck.png" alt="truck" />
      case 'TRAIN':
        return <img width="100" height="100" src="https://img.icons8.com/papercut/120/train.png" alt="train" />
      case 'SHIP':
        return <img width="100" height="100" src="https://img.icons8.com/color/96/cruise-ship.png"
                    alt="cruise-ship" />
      case 'AIRPLANE':
        return <img width="100" height="100" src="https://img.icons8.com/arcade/128/airplane-front-view.png"
                    alt="airplane-front-view" />
      default:
        return null
    }
  }

  useEffect(() => {
    getTransports().then(r => setTransports(r))
    getDrivers().then(r=> setDrivers(r))
  }, [])


  const handleAddDriver = (transport) => {
    setSelectedTransport(transport);
    setVisible(true);
  };

  return (
    <div className="container" style={{height:'80vh'}}>
      <button className="orderSentButton" onClick={() => nav(ADD_TRANSPORT_ROUTE)}>Добавить транспорт</button>
      <h4 className="mt-3">Транспорт: </h4>
      <div className='d-flex g-5'>
        {transports.map(transport => (
          <div className="card_for_transport m-2" key={transport.id}>
            <div>{getIconForTransportType(transport.transportType)}</div>
            <div>Тип: {transport.transportType}</div>
            <div>Грузоподъёмность: {transport.tonnage}</div>
            <div>Объем: {transport.volume}</div>
            {transport.driver ? (
              <div>Водитель: {transport.driver.firstName} {transport.driver.lastName}</div>)
              : (<button className="add_some_button"
                         onClick={() => handleAddDriver(transport)}
                >Добавить водителя</button>)
            }
          </div>)
        )}
      </div>

      <h4 className="mt-3">Перевозчики</h4>

      <button className="orderSentButton">
        <NavLink style={{color: 'white'}} to={`${REGISTRATION_CLIENT_ROUTE}?role=driver`}>
          Добавление перевозчика
        </NavLink>
      </button>
      <div className="d-flex mt-3 mb-3">
        {drivers.map(driver => (
          <div key={driver.id} className='card_for_driver d-flex flex-column align-items-center' style={{marginRight: '10px'}}>
            <Image src='https://img.icons8.com/?size=100&id=nULzKoWMIRQw&format=png&color=000000' style={{width:'100px'}} />
            <div>{driver.firstName} {driver.lastName}</div>
            <div>{driver.phone}</div>
            <div>{driver.email}</div>
          </div>)
        )}
      </div>

      <AddDriver show={visible} onHide={() => setVisible(false)} transport={selectedTransport} />
    </div>
  )
}

export default Equipment
