import {
  ABOUT_ROUTE,
  BASKET_ROUTE,
  LOGIN_ROUTE,
  PAYMENT_ROUTE,
  REGISTRATION_ROUTE,
  GUARANTEES_ROUTE,
  MAIN_ROUTE,
  ACCOUNT_ROUTE,
  ORDER_ROUTE, REGISTRATION_CLIENT_ROUTE, REGISTRATION_PARTNER_ROUTE,
} from './utils/consts'

import Basket from './pages/Basket'
import Main from './pages/Main'
import Auth from './pages/Auth'
import AboutUs from './pages/AboutUs'
import Payment from './pages/Payment'
import Guarantees from './pages/Guarantees'
import Account from './pages/AccountPage'
import Order from "./pages/Order";
import RegisClient from './pages/RegisClient'
import RegisPartner from './pages/RegisPartner'
import Regis from './pages/Regis'

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
    Component: Order,
  },
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