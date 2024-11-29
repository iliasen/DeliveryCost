import React, { useContext, useEffect, useState } from 'react'
import { Context } from '../index'

import { observer } from 'mobx-react-lite'

import '../styles/MainWithoutAuth.css'
import '../styles/ClientWorkspace.css'
import WithoutAuth from '../components/WithoutAuth'
import ClientWorkspace from '../components/ClientWorkspace'
import PartnerWorkspace from '../components/PartnerWorkspace'
import DriverWorkspace from '../components/DriverWorkspace'
import PartnerData from '../components/PartnerData'
import { checkFiends } from '../http/partnerAPI'


const Main = observer(() => {
  const { user } = useContext(Context)
  const [partnerStatus, setPartnerStatus] = useState(false)
  useEffect(() => {
    if(user.Auth && user.user.role === "PARTNER"){
      checkFiends().then(data => setPartnerStatus(data))
    }
  })

  console.log(partnerStatus)
  return (
    <>
      {user.Auth ? (
        <>
          {user.user.role === 'CLIENT' && <ClientWorkspace />}

          {user.user.role === 'PARTNER' && <div>{!partnerStatus ? <PartnerData />: <PartnerWorkspace/>}</div>}
          {user.user.role === "DRIVER" && <DriverWorkspace/>}
        </>
      ) : (
        <WithoutAuth />
      )}
    </>
  )
})

export default Main
