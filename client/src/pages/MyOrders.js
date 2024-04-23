import React, { useContext, useEffect } from 'react'
import { getOrder } from '../http/orderAPI'
import { Context } from '../index'
import OrderItem from '../components/OrderItem'
import { Container } from 'react-bootstrap'
import { observer } from 'mobx-react-lite'
import {Image} from "react-bootstrap";

import emptyOrder from "../res/Basket/empty_basket.png"

import '../styles/MyOrders.css'
import { MAIN_ROUTE } from '../utils/consts'
import { useNavigate } from 'react-router-dom'


const MyOrders = observer(() => {
  const {order} = useContext(Context)
  const navigate = useNavigate()
  useEffect(()=> {
    getOrder().then((orders)=> order.setOrder(orders))
  },[order])

  return (
    <Container className="main-page-container pt-2">
      <div id="orders">
        {order.order.length !== 0 ?
          <div>
            {order.order.map((order) =>
              (<OrderItem key={order.id} order={order} />)
            )}
          </div> : <div className='EmptyBasket'>
            <Image src={emptyOrder} style={{width: 360, height: 320}}/>

            <h1>У вас нет заказов</h1>
            <div>Перейдите на <span onClick={() => navigate(MAIN_ROUTE)}>главную страницу</span> и добавьте заказы.</div>
          </div>}
      </div>
    </Container>
  )
})

export default MyOrders