import React, { useContext, useState } from 'react'
import { Context } from '../index'
import { NavLink, useNavigate } from 'react-router-dom'
import { registrationPartner } from '../http/userAPI'
import { LOGIN_ROUTE, MAIN_ROUTE } from '../utils/consts'
import { Form } from 'react-bootstrap'

const RegisPartner = () => {
  const { user } = useContext(Context)
  const [name, setName] = useState('')
  const [inn, setInn] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [contactNumber, setContactNumber] = useState("");
  let nav = useNavigate()

  const handleChange = (e) => {
    let input = e.target.value;

    // Удалить все нецифровые символы
    input = input.replace(/\D/g, "");

    // всегда добавляем префикс +375
    if (!input.startsWith("375")) {
      input = "375" + input;
    }

    // Формат +375 (xx) xxx-xx-xx
    let formatted = "";
    for (let i = 0; i < input.length; i++) {
      if (i === 0) {
        formatted += "+";
      }
      if (i === 3) {
        formatted += " (";
      }
      if (i === 5) {
        formatted += ") ";
      }
      if (i === 8 || i === 10) {
        formatted += "-";
      }
      formatted += input[i];
    }

    setContactNumber(formatted);
  };

  const click = async (event) => {   event.preventDefault()
    try {
      let data
      data = await registrationPartner(name, inn, email, contactNumber, password)
       user.setUser(data)
       user.setAuth(true)
      nav(MAIN_ROUTE)
    } catch (e) {
      alert(e.response.data.message)
    }
  }

  const back = async (event) => {
    event.preventDefault()
    nav(-1)
  }

  return (
    <div className="auth-partner-container">
      <div className="card-content">
        <a aria-label="Контакт Логистик" href="/" className="Logo"></a>
        <h2 className="title">Регистрация</h2>
        <Form className="auth-form">
          <Form.Label>Имя</Form.Label>
          <Form.Control
            placeholder="Введите название компании"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <Form.Label>ИНН Организации</Form.Label>
          <Form.Control
            placeholder="Введите инн"
            value={inn}
            onChange={(e) => setInn(e.target.value)}
          />
          <Form.Label>Email</Form.Label>
          <Form.Control
            placeholder="Введите email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <Form.Label>Пароль</Form.Label>
          <Form.Control
            type="password"
            placeholder="Введите пароль"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <Form.Label>Контактынй телефон</Form.Label>
          <Form.Control
            type="tel"
            name="phone"
            value={contactNumber}
            onChange={handleChange}
            placeholder="+375 (xx) xxx-xx-xx"
            required
          />
          <button id="registration" onClick={click}>
            <NavLink to={LOGIN_ROUTE}>Зарегистрироваться</NavLink>
          </button>
          <button className='back_button' onClick={back} />
        </Form>
      </div>
    </div>
  )
}

export default RegisPartner