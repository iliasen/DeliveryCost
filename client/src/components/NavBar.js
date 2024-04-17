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
  MAIN_ROUTE, MY_ORDERS_ROUTE,
} from '../utils/consts'

import '../styles/NavBar.css'

import { observer } from 'mobx-react-lite'
import { Image } from 'react-bootstrap'
import logo from '../res/лого_тем.png'
import SearchForm from "./SearchForm";

const NavBar = observer(() => {
  const { user } = useContext(Context)
  // useEffect(() => {
  //   getItems(user.user.id).then((items) => {
  //     const adaptedItems = items.map((item) => ({
  //       id: item.id,
  //       name: item.name,
  //       price: item.price,
  //       quantity: item.quantity,
  //       img: item.img,
  //       about: item.about,
  //       typeId: item.type.id,
  //       brandId: item.brand.id,
  //     }));
  //     basket.setBasket_items(adaptedItems);
  //   });
  // }, []);


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
        <SearchForm/>

        {user.Auth && (
            <Navbar.Toggle aria-controls="responsive-navbar-nav" />
        )}

        <Navbar.Collapse id="responsive-navbar-nav">
          {user.Auth ? (
            <Nav className="href-container">
              <NavLink className="href" to={MY_ORDERS_ROUTE}>
                Заказы
              </NavLink>
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
