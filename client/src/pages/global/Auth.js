import React, { useContext, useEffect, useState } from 'react'
import '../../styles/Auth.css'
import { NavLink, useLocation, useNavigate } from 'react-router-dom'
import { LOGIN_ROUTE, REGISTRATION_ROUTE, MAIN_ROUTE } from '../../utils/consts'
import { login } from '../../http/userAPI'
import { observer } from 'mobx-react-lite'
import { Context } from '../../index'
import { Form } from 'react-bootstrap'

const Auth = observer(() => {
  const location = useLocation()
  const isLogin = location.pathname === LOGIN_ROUTE

  const { user } = useContext(Context)
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  let nav = useNavigate()

  const click = async (event) => {   event.preventDefault()
    try {
      let data
        data = await login(email, password)
        user.setUser(data)
        user.setAuth(true)
        nav(MAIN_ROUTE)
        //window.location.reload()
    } catch (e) {
      alert(e.response.data.message)
    }
  }

  const back = async (event) => {
      event.preventDefault()
      nav(-1)
  }



  return (
    <div className="auth-container">
      <div className="card-content">
        <a aria-label="Гефест" href="/public" className="Logo"></a>
        <h2 className="title">{isLogin ? 'Авторизация' : 'Регистрация'}</h2>
        <Form className="auth-form">
          <Form.Control
            placeholder="Введите email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <Form.Control
            type="password"
            placeholder="Введите пароль"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          {isLogin ? (
            <button id="enter" onClick={click}>
              Войти
            </button>
          ) : (
            <button id="registration" onClick={click}>
              <NavLink to={LOGIN_ROUTE}>Зарегистрироваться</NavLink>
            </button>
          )}
          {isLogin && (
            <button id="registration">
              <NavLink to={REGISTRATION_ROUTE}>Создать аккаунт</NavLink>
            </button>
          )}
          <button className='back_button' onClick={back} />
        </Form>
      </div>
    </div>
  )
})

export default Auth
