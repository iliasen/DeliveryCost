import React, {useContext, useEffect} from 'react'
import {Context} from "../index";

import { observer } from 'mobx-react-lite'

import '../styles/MainWithoutAuth.css'
import '../styles/ClientWorkspace.css'
import WithoutAuth from '../components/WithoutAuth'
import ClientWorkspace from '../components/ClientWorkspace'
import PartnerWorkspace from '../components/modals/PartnerWorkspace'


const Main = observer(() => {
    const { user } = useContext(Context);


  return (
    <>
      {user.Auth ? (
        <>
          {user.user.role === "CLIENT" && <ClientWorkspace />}
          {user.user.role === "PARTNER" && <PartnerWorkspace/>}
        </>
      ) : (
        <WithoutAuth />
      )}
    </>
  );
});

export default Main
