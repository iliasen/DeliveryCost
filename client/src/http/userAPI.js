import { $authHost, $host } from './index'
import jwt_decode from 'jwt-decode'


export const registrationClient = async (firstName, lastName, email, phone, password) => {
  const { data } = await $host.post('api/auth/singUp/client', {
    firstName, lastName, email, phone, password
  })
  console.log("Push to server")
  localStorage.setItem('token', data.token)
  return jwt_decode(data.token)
}

export const registrationDriver = async (firstName, lastName, email, phone, password) => {
  const { data } = await $authHost.post('api/auth/singUp/driver', {
    firstName, lastName, email, phone, password
  })
  console.log("Push to server")
  localStorage.setItem('token', data.token)
  return jwt_decode(data.token)
}


export const registrationPartner = async (companyName, inn, email,contactNumber, password) => {
  const { data } = await $host.post('api/auth/singUp/partner', {
    companyName, inn, contactNumber,email, password
  })
  console.log("Push to server")
  localStorage.setItem('token', data.token)
  return jwt_decode(data.token)
}

export const login = async (email, password) => {
  const { data } = await $host.post('api/auth/singIn', { email, password })
  localStorage.setItem('token', data.token)
  return jwt_decode(data.token)
}

export const getUser = async () => {
  // const {email} = localStorage.getItem('email')
  // const {password} = localStorage.getItem('password')
  // const { data } = await $authHost.post('api/auth/singIn', { email, password })
  // localStorage.setItem('token', data.token)
  // return jwt_decode(data.token)
  return jwt_decode(localStorage.getItem('token'))
}

export const del = async (id) => {
  const { data } = await $authHost.delete('api/user/delete_account/'+ id);
  localStorage.clear();
  return data
}

export const change = async (id,oldPassword, newPassword) => {
  const { data } = await $authHost.put('api/user/change/'+{id}, {oldPassword, newPassword})
  localStorage.setItem('token', data.token)
  return jwt_decode(data.token)
}