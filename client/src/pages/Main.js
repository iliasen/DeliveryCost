import React, {useContext, useEffect} from 'react'
import {Context} from "../index";
import {NavLink} from "react-router-dom";
import { LOGIN_ROUTE, REGISTRATION_CLIENT_ROUTE, REGISTRATION_PARTNER_ROUTE } from '../utils/consts'
import {Image} from 'react-bootstrap'
import {getPartners} from "../http/partnerAPI";
import { observer } from 'mobx-react-lite'

import box from "../res/Main/box.svg"
import truck from "../res/Main/car.svg"
import human from "../res/Main/human.svg"
import safe from "../res/Main/safe1.png"
import free from "../res/Main/free1.png"


import '../styles/MainWithoutAuth.css'
import '../styles/MainWithAuth.css'
import Rating from "../components/modals/Rating";
import {getAverageRating} from "../http/itemAPI";


const Main = observer(() => {
    const { user, partners } = useContext(Context);

    useEffect(() => {
        if(user.Auth){
            getPartners().then((partnersData) => {
                partners.setPartners(partnersData);
                console.log(partnersData)
            });
        }
    }, []);



  return (
      <>
          {user.Auth ?
              <div className="main-page-container">
                  <div className="d-flex p-1">
                      <div className="partner-list">
                          <h4>Наши партнёны</h4>
                          {partners.partners && partners.partners.map((partner) => (
                              <div key={partner.id} className="partner-item"
                                   autoFocus={partner.id === partners.selectedPartner?.id}
                                   onClick={() => partners.setPartner(partner)}>
                                  <p>companyName-{partner.companyName}</p>
                                  <p>email-{partner.email}</p>
                                  <p>Tel--{partner.contactNumber}</p>
                              </div>
                          ))}
                      </div>
                      <div className="partner-info">
                          {partners.selectedPartner !== null ?
                              <div>
                                  <p>ID: {partners.selectedPartner.id}</p>
                                  <p>INN: {partners.selectedPartner.inn}</p>
                                  <p>Company Name: {partners.selectedPartner.companyName}</p>
                                  <p>Email: {partners.selectedPartner.email}</p>
                                  <p>Contact Number: {partners.selectedPartner.contactNumber}</p>
                                  <p>Company Official: {partners.selectedPartner.companyOfficial}</p>

                                  <Rating rating={}/>
                              </div>
                              :
                              <div>No partner is Selected</div>
                          }
                      </div>
                  </div>
              </div>

              :
              <div className="main-page-container">
                  <div id="preview">
                      <div className="register-wrapper">
                          Крупнейшая международная<br/>
                          Биржа грузоперевозок Контакт Логистик
                          <div>
                              Помогает перевозчикам и грузоотправителям из Беларуси, России<br/>и ещё 60 стран найти
                              друг друга
                              и
                              договориться о перевозке
                          </div>
                      </div>
                      <button>
                          <NavLink to={LOGIN_ROUTE}>Авторизироваться</NavLink>
                      </button>
                  </div>

                    <div id="our-stats">
                        <div className='boxes'>
                            <h4>350 000 грузов</h4>
                            <div>
                                <Image src={box}/>
                                <span>
                                  ждут откликов <br/>от перевозчиков
                              </span>
                            </div>
                        </div>
                        <div className='boxes'>
                            <h4>100 000 машин</h4>
                            <div>
                                <Image src={truck}/>
                                <span>
                                  готовы <br/>к перевозке <br/>прямо сейчас
                              </span>
                            </div>
                        </div>
                        <div className='boxes'>
                            <h4>220 000 участников</h4>
                            <div>
                                <Image src={human}/>
                                <span>
                                  ежедневно <br/>работают <br/>на KLog.by
                              </span>
                            </div>
                        </div>
                    </div>

                    <div className="partners">
                        <h1>Находите надёжных партнёров на Klog.by</h1>
                        <div>
                            <div className='why-we'>
                                <h4>Безопасно</h4>
                                <div>
                                    <ul>
                                        <li>
                                            Паспорт участника ATI.SU отражает его репутацию и показывает, можно ли ему
                                            доверять.
                                        </li>
                                        <li>
                                            Рейтинг участников ATI.SU помогает найти лучших в вашей стране или регионе.
                                        </li>
                                        <li>
                                            Бесплатный GPS-мониторинг грузов и транспорта в реальном времени
                                        </li>
                                    </ul>
                                    <Image src={safe} style={{height: 200, width: 200}}/>
                                </div>
                            </div>

                            <div className='why-we'>
                                <h4>Бесплатно</h4>
                                <div>
                                    <ul>
                                        <li>
                                            Регистрация.
                                        </li>
                                        <li>
                                            Добавление товаров и транспорта.
                                        </li>
                                        <li>
                                            Мониторинг товара.
                                        </li>
                                        <li>
                                            Поиск грузов.
                                        </li>

                                    </ul>
                                    <Image src={free} style={{height: 180}}/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="welcome">
                        <h2>
                            Начните работать на KLog.by
                            <br/>в Беларуси — это бесплатно
                        </h2>
                        <div>
                            <button id="reg-partner">
                                <NavLink to={REGISTRATION_PARTNER_ROUTE}>Для Перевозчиков</NavLink>
                            </button>
                            <button id="reg-client">
                                <NavLink to={REGISTRATION_CLIENT_ROUTE}>Для Грузовладельцев</NavLink>
                            </button>
                        </div>
                    </div>
                </div>
          }
      </>

    )
})

export default Main
