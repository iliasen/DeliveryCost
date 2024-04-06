import React, { useContext, useEffect, useRef, useState } from 'react'
import { Container, Spinner } from 'react-bootstrap'
import {Context} from "../index";
import {NavLink} from "react-router-dom";
import {BASKET_ROUTE} from "../utils/consts";
import {observer} from "mobx-react-lite";

import ConfirmTel from "../components/modals/ConfirmTel";
import { addOrder, getAddress } from '../http/orderAPI'
import OrderCheck from "../components/modals/OrderCheck";

import '../styles/Order.css'
import axios from 'axios'




const Order = observer( () => {
    const {user} = useContext(Context)
    const {number} = useContext(Context)
    const [visible, setVisible] = useState(false)
    const [confirmVisible, setConfirmVisible] = useState(false)

    const [pointOfDeparture, setPointOfDeparture] = useState(null)
    const [deliveryPoint, setDeliveryPoint] = useState(null)
    const [distance, setDistance] = useState(null)
    const [deliveryTime,setDeliveryTime] = useState(null)
    const [transportType, setTransportType] = useState(null)
    const [comment, setComment] = useState(null)
    const [weight, setWeight] = useState(null)
    const [value, setValue] = useState(null)
    const [departureSearchResults, setDepartureSearchResults] = useState([]);
    const [deliverySearchResults, setDeliverySearchResults] = useState([]);

    const innerFormRef = useRef(null)




    useEffect(() => {
        let timeoutId;

        const handleDepartureSearch = () => {
            if (pointOfDeparture) {
                getAddress(pointOfDeparture).then((data) => {
                    const items = data.result ? data.result.items : [];
                    setDepartureSearchResults(items);
                });
            } else {
                setDepartureSearchResults([]);
            }
        };

        clearTimeout(timeoutId);
        timeoutId = setTimeout(handleDepartureSearch, 5000);

        return () => {
            clearTimeout(timeoutId);
        };
    }, [pointOfDeparture]);

    useEffect(() => {
        let timeoutId;

        const handleDeliverySearch = () => {
            if (deliveryPoint) {
                getAddress(deliveryPoint).then((data) => {
                    const items = data.result ? data.result.items : [];
                    setDeliverySearchResults(items);
                });
            } else {
                setDeliverySearchResults([]);
            }
        };

        clearTimeout(timeoutId);
        timeoutId = setTimeout(handleDeliverySearch, 5000);

        return () => {
            clearTimeout(timeoutId);
        };
    }, [deliveryPoint]);



    const handleChange = (e) => {
        let input = e.target.value;

        // Удалить все нецифровые символы
        input = input.replace(/\D/g, "");

        // всегда добавляем префикс +375
        if (!input.startsWith("375")) {
            input = "375" + input;
        }

        // Формат +375 (xx) xxx-xx-xx
        let formatted = "";
        for (let i = 0; i < input.length; i++) {
            if (i === 0) {
                formatted += "+";
            }
            if (i === 3) {
                formatted += " (";
            }
            if (i === 5) {
                formatted += ") ";
            }
            if (i === 8 || i === 10) {
                formatted += "-";
            }
            formatted += input[i];
        }

        setValue(formatted);
    };

    const handleDepartureResultClick = (result) => {
        setPointOfDeparture(result.full_name);
        setDepartureSearchResults([]);
    };

    const handleDeliveryResultClick = (result) => {
        setDeliveryPoint(result.full_name);
        setDeliverySearchResults([]);
    };

    const submitInnerForm = (event) => {
        event.preventDefault();
        setConfirmVisible(true)
    }

    const submitOrder = (event) => {
        event.preventDefault();
        addOrder(user.user, comment).then(()=>setVisible(true))
    }


    return (
        <Container className='container-shop'>
            <h2 className='mt-5'>Оформление заказа</h2>
            <div className='orderContainer'>
                <form id='OrderForm' onSubmit={submitOrder}>
                    <div className='d-flex gap-5'>
                        <div>
                            <div>
                                Откуда забрать товар
                                <div className='d-flex align-items-center'>
                                    <input className='Location' type="text" placeholder='Введите ваш пункт забора'
                                           required
                                           value={pointOfDeparture}
                                           onChange={(e) => setPointOfDeparture(e.target.value)} />
                                    <input type='reset' className='resetLocationButton' value='x'
                                           onClick={() => setPointOfDeparture(null)}></input>
                                </div>

                                {pointOfDeparture && departureSearchResults.length === 0 ? (
                                  <Spinner style={{ margin: "16px 32%", display: "block" }}/>
                                ) : (
                                  <>
                                      {/* Отображение результатов поиска */}
                                      {departureSearchResults.length > 0 && (
                                        <div className="searchResultsContainer">
                                            <h4>Результаты поиска:</h4>
                                            {departureSearchResults.map((item) => (
                                              <div key={item.id} className="searchResults" onMouseDown={() => handleDepartureResultClick(item)}>
                                                  <p>Адрес: {item.full_name}</p>
                                                  <p>Координаты: {item.point.lat}, {item.point.lon}</p>
                                                  <p>Тип: {item.type}</p>
                                                  <p>Назначение: {item.purpose_name}</p>
                                              </div>
                                            ))}
                                        </div>
                                      )}
                                  </>
                                )}
                            </div>

                            <div className='deliveryAddress'>
                                <span style={{
                                    color: '#999',
                                    fontSize: 13
                                }}>Укажите как можно подробнее адрес забора товара</span>

                                <div className='d-flex'>
                                    <div className='addressInputs'>
                                        <label>Вес груза(кг.)</label>
                                        <input className="Flat" name="weidth" type="number" required value={weight} onChange={(e)=> setWeight(e.target.value)}/>
                                    </div>
                                </div>
                            </div>
                            <div className='d-flex flex-column mt-4'>
                                <label>Коментарий к заказу <span
                                  style={{ color: '#999' }}>(необязательно)</span></label>
                                <textarea className='orderComment' rows='3' placeholder='Укажите дополнительные детали'
                                          name='orderDetails' value={comment}
                                          onChange={(e) => setComment(e.target.value)} />
                                <span className='mt-2' style={{ maxWidth: 480, fontSize: 14, color: '#999' }}>Расскажите информацию о заказе, подъезд, укажите код домофона или другую информацию, которая может пригодиться курьеру.</span>
                            </div>

                            {number.number === '' && <div className='telNumber'>
                                <strong>Телефон</strong>
                                <div style={{ fontSize: 14 }}>Подтвердите ваш номер телефона, на него будет отправлено
                                    SMS с кодом !
                                </div>
                                <div className='d-flex mt-2'>
                                    <form ref={innerFormRef}>
                                        <input
                                          type="tel"
                                          id="phone"
                                          name="phone"
                                          value={value}
                                          onChange={handleChange}
                                          placeholder="+375 (xx) xxx-xx-xx"
                                          required
                                        />
                                        <button
                                          type='button'
                                          className='confirmTel'
                                          disabled={!value}
                                          onClick={submitInnerForm}
                                        >
                                            Подтвердить номер
                                        </button>
                                    </form>
                                </div>
                                <div style={{ color: '#999', fontSize: 14 }} className='mt-1'>Например +375 (29)
                                    842-05-07
                                </div>
                            </div>}

                            <button className='orderSentButton' type='submit'>Заказать</button>
                        </div>

                        <div>
                            <div>
                                Куда доставить товар
                                <div className='d-flex align-items-center'>
                                    <input className='Location' type="text" placeholder='Введите пункт доставки'
                                           defaultValue={deliveryPoint} required
                                           onChange={(e) => setDeliveryPoint(e.target.value)} />
                                    <input type='reset' className='resetLocationButton' value='x'
                                           onClick={() => setDeliveryPoint(null)}></input>
                                </div>
                                {deliveryPoint && deliverySearchResults.length === 0 ? (
                                  <Spinner style={{ margin: "16px auto", display: "block" }} />
                                ) : (
                                  <>
                                      {deliverySearchResults.length > 0 && (
                                        <div className="searchResultsContainer">
                                            <h4>Результаты поиска:</h4>
                                            {deliverySearchResults.map((item) => (
                                              <div key={item.id} className="searchResults" onMouseDown={() => handleDeliveryResultClick(item)}>
                                                  <p>Адрес: {item.full_name}</p>
                                                  <p>Координаты: {item.point.lat}, {item.point.lon}</p>
                                                  <p>Тип: {item.type}</p>
                                                  <p>Назначение: {item.purpose_name}</p>
                                              </div>
                                            ))}
                                        </div>
                                      )}
                                  </>
                                )}
                            </div>

                            <div className='deliveryAddress'>
                                <span style={{
                                    color: '#999',
                                    fontSize: 13
                                }}>Укажите как можно подробнее адрес доставки</span>
                                {/*<div className='d-flex'>*/}
                                {/*    <div className='addressInputs'>*/}
                                {/*        <label>Улица</label>*/}
                                {/*        <input className='Address' name='street' type='text' placeholder='Введите улицу'*/}
                                {/*               value={street} onChange={(e) => setStreet(e.target.value)} required />*/}
                                {/*    </div>*/}
                                {/*    <div className='addressInputs'>*/}
                                {/*        <label>Дом</label>*/}
                                {/*        <input className='House' name='house' value={house}*/}
                                {/*               onChange={(e) => setHouse(e.target.value)} required />*/}
                                {/*    </div>*/}
                                {/*    <div className='addressInputs'>*/}
                                {/*        <div className='d-flex'><label>Корп.</label> <span*/}
                                {/*          style={{ color: '#999', fontSize: 12 }}>(необязательно)</span></div>*/}
                                {/*        <input className='House' name='Corpus' value={corpus}*/}
                                {/*               onChange={(e) => setCorpus(e.target.value)} />*/}
                                {/*    </div>*/}
                                {/*</div>*/}

                                {/*<div className='d-flex'>*/}
                                {/*    <div className='addressInputs'>*/}
                                {/*        <label>Под.</label>*/}
                                {/*        <input className='House' name='entrance' value={entrance}*/}
                                {/*               onChange={(e) => setEntrance(e.target.value)} required />*/}
                                {/*    </div>*/}
                                {/*    <div className='addressInputs'>*/}
                                {/*        <label>Этаж</label>*/}
                                {/*        <input className='House' name='floor' value={floor}*/}
                                {/*               onChange={(e) => setFloor(e.target.value)} required />*/}
                                {/*    </div>*/}
                                {/*    <div className='addressInputs'>*/}
                                {/*        <label>Квартира</label>*/}
                                {/*        <input className='Flat' name='flat' value={flat}*/}
                                {/*               onChange={(e) => setFlat(e.target.value)} required />*/}
                                {/*    </div>*/}
                                {/*    <div className='addressInputs'>*/}
                                {/*        <label>Вес груза(кг.)</label>*/}
                                {/*        <input className="Flat" name="weidth" type="number" required />*/}
                                {/*    </div>*/}
                                {/*</div>*/}
                            </div>

                        </div>
                    </div>

                </form>


                {/*<div className='order'>*/}
                {/*    <div className='d-flex justify-content-between'>*/}
                {/*        <h5>Заказ</h5>*/}
                {/*        <NavLink className='changeOrder' to={BASKET_ROUTE}>Изменить</NavLink>*/}
                {/*    </div>*/}
                {/*    <div>*/}
                {/*        {basket.basket_items.map((item) => (*/}
                {/*            <div className='itemsContainer'>*/}
                {/*                <div key={item.id} className='itemsFromBasket'>*/}
                {/*                    <div className='item-in-order'>*/}
                {/*                        {item.name}{item.quantity !==1 ? ' ('+item.quantity+' шт.)': null}*/}
                {/*                    </div>*/}

                {/*                    <div style={{fontSize: 16, lineHeight: 1.4}}>{item.price * item.quantity},00 р.</div>*/}

                {/*                </div>*/}
                {/*                <div  style={{color: '#999', textAlign: "end", fontSize: 14}}>*/}
                {/*                    {item.quantity !==1 ? item.price + ' р./шт.': null}*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        ))}*/}
                {/*    </div>*/}

                {/*    <div className='finalPrice'>*/}
                {/*        <div className='d-flex justify-content-between mb-2' style={{color: '#999'}}>Доставка <div style={{fontSize: 16, lineHeight: 1.4, color: '#000'}}>{delivery === 0 ? 'бесплатно' : delivery +',00 р.'}</div></div>*/}
                {/*        <div className='d-flex justify-content-between'><span style={{fontWeight: 500}}>Итого</span><strong>{finalPrice},00 р.</strong></div>*/}
                {/*    </div>*/}
                {/*</div>*/}
            </div>

            <ConfirmTel show={confirmVisible} onHide={() => setConfirmVisible(false)} />
            <OrderCheck show={visible} onHide={() => setVisible(false)} />
        </Container>
    );
});

export default Order;

