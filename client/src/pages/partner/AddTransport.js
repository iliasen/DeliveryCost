import React, { useState } from 'react'
import { addTransport } from '../../http/transportAPI'
import { useNavigate } from 'react-router-dom'
import { MAIN_ROUTE } from '../../utils/consts'

const AddTransport = () => {

  const [selectedType, setSelectedType] = useState(null)
  const [weight, setWeight] = useState(null)
  const [volume, setVolume] = useState(null)

  let nav = useNavigate()


  const transportTypes = ['CAR', 'TRUCK', 'TRAIN', 'SHIP', 'AIRPLANE'];

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

  function addNewTransport() {
    addTransport(selectedType, weight, volume).then()
    alert("Транспорт успешно добавлен")
    nav(-1)
  }

  return (
    <div className='container' style={{"height":"70vh"}} >
      <h1 className="mt-5">Добавление транспорта</h1>

      <h5 className="mt-4 f">Тип доставки</h5>
      <div className="d-flex gap-5">
        {transportTypes.map((type) => (
          <div key={type} className={`deliveryType ${selectedType === type && 'selected'}`}
               onClick={() => setSelectedType(type)}>
            {getIconForTransportType(type)}
            {type}
          </div>
        ))}
      </div>

      <h5 className="mt-5 f">Парамерты транспорта</h5>
      <div className="d-flex gap-5">
        <div className="addressInputs">
          <label>Грузоемкость (кг.)</label>
          <input className="weight" name="weight" type="number" required value={weight}
                 onChange={(e) => setWeight(e.target.value)} />
        </div>
        <div className='addressInputs'>
          <label>Объем (см.^3)</label>
          <input className='weight' name="volume" type='number' required value={volume}
                 onChange={(e) => setVolume(e.target.value)} />
        </div>

      </div>
        <button type="submit" className="orderSentButton false" onClick={()=> addNewTransport()}>Добавить</button>
    </div>
  )
}

export default AddTransport
