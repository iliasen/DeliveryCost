import React, { useContext, useEffect, useState } from 'react'
import { BrowserRouter } from 'react-router-dom'
import AppRouter from './components/AppRouter'
import NavBar from './components/NavBar'
import { Footer } from './components/Footer'
import { observer } from 'mobx-react-lite'
import {getUser} from './http/userAPI'
import { Spinner } from 'react-bootstrap'
import { Context } from './index'
const App = observer(() => {
  const { user } = useContext(Context)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
      getUser()
      .then((data) => {
        user.setUser(data)
        user.setAuth(true)
      })
      .finally(() => setLoading(false))
  }, [])

  if (loading) {
    return <Spinner animation={'grow'} /> // пока ждём ответ(проверка нашего локального токена) появляется загрузка
  }
  return (
    <BrowserRouter>
      <NavBar />
      <AppRouter />
      <Footer />
    </BrowserRouter>
  )
})

export default App
