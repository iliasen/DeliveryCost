import React, {useContext, useEffect} from 'react'
import { Context } from '../index'
import Container from 'react-bootstrap/Container'
import Nav from 'react-bootstrap/Nav'
import Navbar from 'react-bootstrap/Navbar'
import { NavLink } from 'react-router-dom'
import {
  ABOUT_ROUTE,
  ACCOUNT_ROUTE,
  LOGIN_ROUTE,
  MAIN_ROUTE, MY_ORDERS_ROUTE, RESOLVE_PROBLEM, WAREHOUSE_ROUTE,
} from '../utils/consts'

import '../styles/NavBar.css'

import { observer } from 'mobx-react-lite'
import { Image } from 'react-bootstrap'
import logo from '../res/лого_тем.png'
import Notify from "./Notifications";

const NavBar = observer(() => {
  const { user } = useContext(Context)



  return (
    <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
      <Container>
        <Image className="image" src={logo}></Image>
        <NavLink className="brand" to={MAIN_ROUTE}>
          Контакт Логистик
        </NavLink>
        <div className="slogan">Придумай слоган сам</div>
        <NavLink className="href" to={ABOUT_ROUTE}>
          О нас
        </NavLink>

        {user.Auth && (
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        )}

        <Navbar.Collapse id="responsive-navbar-nav">
          {user.Auth ? (
            <Nav className="href-container">
              {user.user.role === "PARTNER" &&
              <NavLink className="href" to={RESOLVE_PROBLEM}>
                Оптимизация
              </NavLink>}

              {user.user.role === "CLIENT" &&
                <NavLink className="href" to={WAREHOUSE_ROUTE}>
                  Склад
                </NavLink>}

              <NavLink className="href" to={MY_ORDERS_ROUTE}>
                Заказы
              </NavLink>

              <Notify/>

              <NavLink className="href d-flex" to={ACCOUNT_ROUTE}>
                  <div className='profile_image' />
                  {user.user.sub}
              </NavLink>
            </Nav>
          ) : (
            <Nav className="href-container">
              <NavLink className="href" to={LOGIN_ROUTE}>
                Авторизация
              </NavLink>
            </Nav>
          )}
        </Navbar.Collapse>
      </Container>
    </Navbar>
  )
})

export default NavBar
