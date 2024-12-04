import {
  ABOUT_ROUTE,
  LOGIN_ROUTE,
  PAYMENT_ROUTE,
  REGISTRATION_ROUTE,
  GUARANTEES_ROUTE,
  MAIN_ROUTE,
  ACCOUNT_ROUTE,
  ORDER_ROUTE,
  REGISTRATION_CLIENT_ROUTE,
  REGISTRATION_PARTNER_ROUTE,
  MY_ORDERS_ROUTE,
  RESOLVE_PROBLEM,
  WAREHOUSE_ROUTE, ADD_TRANSPORT_ROUTE, EQUIPMENT_ROUTE, PARTNER_TRANSFER_ROUTE,
} from './utils/consts'


import Main from './pages/Main'
import Auth from './pages/global/Auth'
import AboutUs from './pages/global/AboutUs'
import Payment from './pages/global/Payment'
import Guarantees from './pages/global/Guarantees'
import Account from './pages/global/AccountPage'
import DoOrder from "./pages/client/DoOrder";
import RegisClient from './pages/auth/RegisClient'
import RegisPartner from './pages/auth/RegisPartner'
import Regis from './pages/auth/Regis'
import MyOrders from './pages/MyOrders'
import BackProblem from './pages/partner/BackProblem'
import Warehouse from './pages/client/Warehouse'
import AddTransport from './pages/partner/AddTransport'
import Equipment from './pages/partner/Equipment'
import TransferOrders from './pages/partner/TransferOrders'

export const authRoutes = [
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
  },
  {
    path: WAREHOUSE_ROUTE,
    Component: Warehouse
  },
  {
    path: EQUIPMENT_ROUTE,
    Component: Equipment
  },
  {
    path: ADD_TRANSPORT_ROUTE,
    Component: AddTransport
  },
  {
    path: PARTNER_TRANSFER_ROUTE,
    Component: TransferOrders
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
