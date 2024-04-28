import {
  ABOUT_ROUTE,
  BASKET_ROUTE,
  LOGIN_ROUTE,
  PAYMENT_ROUTE,
  REGISTRATION_ROUTE,
  GUARANTEES_ROUTE,
  MAIN_ROUTE,
  ACCOUNT_ROUTE,
  ORDER_ROUTE, REGISTRATION_CLIENT_ROUTE, REGISTRATION_PARTNER_ROUTE, MY_ORDERS_ROUTE, RESOLVE_PROBLEM,
} from './utils/consts'

import Basket from './pages/Basket'
import Main from './pages/Main'
import Auth from './pages/Auth'
import AboutUs from './pages/AboutUs'
import Payment from './pages/Payment'
import Guarantees from './pages/Guarantees'
import Account from './pages/AccountPage'
import DoOrder from "./pages/DoOrder";
import RegisClient from './pages/RegisClient'
import RegisPartner from './pages/RegisPartner'
import Regis from './pages/Regis'
import MyOrders from './pages/MyOrders'
import BackProblem from './pages/BackProblem'

export const authRoutes = [
  {
    path: BASKET_ROUTE,
    Component: Basket,
  },
  {
    path: ACCOUNT_ROUTE,
    Component: Account,
  },
  {
    path: ORDER_ROUTE,
    Component: DoOrder,
  },
  {
    path: MY_ORDERS_ROUTE,
    Component: MyOrders
  },
  {
    path: RESOLVE_PROBLEM,
    Component: BackProblem
  }
]

export const publicRoutes = [
  {
    path: MAIN_ROUTE,
    Component: Main,
  },
  {
    path: LOGIN_ROUTE,
    Component: Auth,
  },
  {
    path: REGISTRATION_ROUTE,
    Component: Regis,
  },
  {
    path: REGISTRATION_CLIENT_ROUTE,
    Component: RegisClient,
  },
  {
    path: REGISTRATION_PARTNER_ROUTE,
    Component: RegisPartner,
  },
  {
    path: ABOUT_ROUTE,
    Component: AboutUs,
  },
  {
    path: PAYMENT_ROUTE,
    Component: Payment,
  },
  {
    path: GUARANTEES_ROUTE,
    Component: Guarantees,
  }
]