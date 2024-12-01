import React from 'react'
import "../../styles/Regis.css"
import { NavLink, useNavigate } from 'react-router-dom'
import { REGISTRATION_CLIENT_ROUTE, REGISTRATION_PARTNER_ROUTE } from '../../utils/consts'

const Regis = () => {
  let nav = useNavigate()
  const back = async (event) => {
    event.preventDefault()
    nav(-1)
  }

  return (
    <div className="choice-container">
      <button className='back_button' onClick={back} />
      <h1>Регистрация нового участника</h1>
      <h3>Ваш профиль</h3>
      <div>
        <button>
          <NavLink to={REGISTRATION_PARTNER_ROUTE}>Перевозчик</NavLink>
        </button>
        <div className="line" />
        <button>
          <NavLink to={`${REGISTRATION_CLIENT_ROUTE}?role=client`}>Грузовладелец</NavLink>
        </button>
      </div>
    </div>
  )
}

export default Regis
