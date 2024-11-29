import React, { useContext, useEffect, useRef, useState } from 'react'
import { Container, Spinner } from 'react-bootstrap'
import {Context} from "../index";
import {observer} from "mobx-react-lite";

import ConfirmTel from "../components/modals/ConfirmTel";
import { addOrder, getAddress, getDistance } from '../http/orderAPI'
import OrderCheck from "../components/modals/OrderCheck";

import '../styles/DoOrder.css'


const DoOrder = observer( () => {
    const {partners} = useContext(Context)
    const {number} = useContext(Context)
    const [visible, setVisible] = useState(false)
    const [confirmVisible, setConfirmVisible] = useState(false)

    const [pointOfDeparture, setPointOfDeparture] = useState(null)
    const [deliveryPoint, setDeliveryPoint] = useState(null)
    const [distance, setDistance] = useState(null)
    const [deliveryTime,setDeliveryTime] = useState(null)
    const [comment, setComment] = useState(null)
    const [weight, setWeight] = useState(null)
    const [length, setLength] = useState(null)
    const [width, setWidth] = useState(null)
    const [height, setHeight] = useState(null)

    const [value, setValue] = useState(null)
    const [departureSearchResults, setDepartureSearchResults] = useState([]);
    const [deliverySearchResults, setDeliverySearchResults] = useState([]);
    const [isDepartureSearching, setIsDepartureSearching] = useState(false);
    const [isDeliverySearching, setIsDeliverySearching] = useState(false);
    const transportTypes = ['CAR', 'TRUCK', 'TRAIN', 'SHIP', 'AIRPLANE'];
    const transportPrices = {
        'CAR': 0.5,
        'TRUCK': 0.8,
        'TRAIN': 0.3,
        'SHIP': 1.2,
        'AIRPLANE': 2.0
    };
    const weightLimit = {
        'CAR': 1000,
        'TRUCK': 5000,
        'TRAIN': 100000,
        'SHIP': 1000000,
        'AIRPLANE': 1500
    }
    const [selectedType, setSelectedType] = useState(null);
    const innerFormRef = useRef(null)

    const transportWeight = weightLimit[selectedType];

     const calculateShippingCost = ()=> {
        if (distance === null || selectedType === null) {
            return null; // Возвращаем null, если значения distance или selectedType не определены
        }

        if (!transportPrices.hasOwnProperty(selectedType)) {
            throw new Error("Invalid transport type");
        }

        const transportPrice = transportPrices[selectedType];
        const baseCost = distance.routes[0].distance * transportPrice;
        const supplierCost = baseCost * (1 + partners.selectedPartner.margin/100);

        return Math.round(supplierCost);
    }

    console.log(calculateShippingCost())

    useEffect(() => {
        let isMounted = true;
        let timeoutId;

        const handleDepartureSearch = () => {
            if (pointOfDeparture) {
                setIsDepartureSearching(true); // Устанавливаем флаг поиска отправления

                clearTimeout(timeoutId); // Отменяем предыдущий запрос, если есть

                timeoutId = setTimeout(() => {
                    getAddress(pointOfDeparture)
                      .then((data) => {
                          if (isMounted) {
                              const items = data.result ? data.result.items : [];
                              setDepartureSearchResults(items);
                          }
                      })
                      .finally(() => {
                          if (isMounted) {
                              setIsDepartureSearching(false); // Сбрасываем флаг поиска отправления
                          }
                      });
                }, 3000); // Задержка в 3 секунду перед выполнением запроса
            } else {
                setDepartureSearchResults([]);
            }
        };

        handleDepartureSearch();

        return () => {
            isMounted = false;
            clearTimeout(timeoutId); // Очищаем таймер при размонтировании компонента
        };
    }, [pointOfDeparture]);

    useEffect(() => {
        let isMounted = true;
        let timeoutId;

        const handleDeliverySearch = () => {
            if (deliveryPoint) {
                setIsDeliverySearching(true);

                clearTimeout(timeoutId);

                timeoutId = setTimeout(() => {
                    getAddress(deliveryPoint)
                      .then((data) => {
                          if (isMounted) {
                              const items = data.result ? data.result.items : [];
                              setDeliverySearchResults(items);
                          }
                      })
                      .finally(() => {
                          if (isMounted) {
                              setIsDeliverySearching(false);
                          }
                      });
                }, 3000);
            } else {
                setDeliverySearchResults([]);
            }
        };

        handleDeliverySearch();

        return () => {
            isMounted = false;
            clearTimeout(timeoutId); // Очищаем таймер при размонтировании компонента
        };
    }, [deliveryPoint]);

    console.log(departureSearchResults[0], deliverySearchResults[0])

    useEffect(() => {
        if (departureSearchResults.length > 0 && deliverySearchResults.length > 0) {
            const departurePoint = departureSearchResults[0].point;
            const deliveryPoint = deliverySearchResults[0].point;
            const points = [
                {
                    lat: departurePoint.lat,
                    lon: departurePoint.lon
                },
                {
                    lat: deliveryPoint.lat,
                    lon: deliveryPoint.lon
                }
            ];

            getDistance(points)
              .then((distanceData) => {
                  console.log('Результаты расчета расстояния:', distanceData);
                  setDistance(distanceData);
              })
              .catch((error) => {
                  console.error('Ошибка:', error);
              });
        }
    }, [departureSearchResults, deliverySearchResults]);

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

    const getIconForTransportType = (type) => {
        switch (type) {
            case 'CAR':
                return <img width="100" height="100" src="https://img.icons8.com/papercut/120/car.png" alt="car" />
            case 'TRUCK':
                return <img width="100" height="100" src="https://img.icons8.com/papercut/120/truck.png" alt="truck" />
            case 'TRAIN':
                return <img width="100" height="100" src="https://img.icons8.com/papercut/120/train.png" alt="train" />
            case 'SHIP':
                return <img width="100" height="100" src="https://img.icons8.com/color/96/cruise-ship.png"
                            alt="cruise-ship" />
            case 'AIRPLANE':
                return <img width="100" height="100" src="https://img.icons8.com/arcade/128/airplane-front-view.png"
                            alt="airplane-front-view" />
            default:
                return null
        }
    }

    const handleDepartureResultClick = (result) => {
        setPointOfDeparture(result.full_name)
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
        addOrder(partners.selectedPartner.id, pointOfDeparture, deliveryPoint, distance.routes[0].distance, selectedType, comment, weight, length, width, height, calculateShippingCost()).then(()=>setVisible(true))
    }

    return (
        <Container className='container-shop'>
            <h2 className="mt-5">Оформление заказа</h2>
            <div className="orderContainer">
                <form id="OrderForm" onSubmit={submitOrder}>
                    <div className="d-flex gap-5">
                        <div>
                            <div className='d-flex justify-content-between'>
                                <div>
                                    Откуда забрать товар
                                    <div className="d-flex align-items-center">
                                        <input className="Location" type="text" placeholder="Введите ваш пункт забора"
                                               required
                                               value={pointOfDeparture}
                                               onChange={(e) => setPointOfDeparture(e.target.value)} />
                                        <input type="reset" className="resetLocationButton" value="x"
                                               onClick={() => {
                                                   setPointOfDeparture(null);
                                                   setDistance(null);
                                               }}/>
                                    </div>

                                    {isDepartureSearching ? (
                                      <Spinner style={{ margin: '16px 17%', display: 'block' }} />
                                    ) : (
                                      <>
                                          {/* Отображение результатов поиска */}
                                          {departureSearchResults.length > 0 && (
                                            <div className="searchResultsContainer">
                                                <h4>Результаты поиска:</h4>
                                                {departureSearchResults.map((item) => (
                                                  <div key={item.id} className="searchResults"
                                                       onMouseDown={() => handleDepartureResultClick(item)}>
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

                                {distance &&
                                  <div className='m-5'>
                                      <h4>Дистанция</h4>
                                      {distance.routes.map((route, index) => (
                                        <div key={index} style={{textAlign: 'center'}}>
                                            {route.distance} м.
                                        </div>
                                      ))}
                                  </div>
                                }
                            </div>


                            <div className="deliveryAddress">
                                <span style={{
                                    color: '#999',
                                    fontSize: 13,
                                }}>Укажите как можно подробнее адрес забора товара</span>
                                
                                <h4 className='mt-5'>Детали груза</h4>
                                <div className="d-flex">
                            
                                    <div className="addressInputs">
                                        <label>Вес груза(кг.)</label>
                                        <input className="weight" name="weight" type="number" required value={weight}
                                               onChange={(e) => setWeight(e.target.value)} />
                                    </div>
                                    <div className='addressInputs'>
                                        <label>Длина(см.)</label>
                                        <input className='weight' name="length" type='number' required value={length} 
                                            onChange={(e)=> setLength(e.target.value)}/>
                                    </div>
                                    <div className='addressInputs'>
                                        <label>Ширина(см.)</label>
                                        <input className='weight' name="width" type='number' required value={width} 
                                            onChange={(e)=> setWidth(e.target.value)}/>
                                    </div>
                                    <div className='addressInputs'>
                                        <label>Высота(см.)</label>
                                        <input className='weight' name="height" type='number' required value={height}
                                            onChange={(e)=> setHeight(e.target.value)}/>
                                    </div>
                                </div>
                            </div>
                            <div className="d-flex flex-column mt-4">
                                <label>Коментарий к заказу <span
                                  style={{ color: '#999' }}>(необязательно)</span></label>
                                <textarea className="orderComment" rows="3" placeholder="Укажите дополнительные детали"
                                          name="orderDetails" value={comment}
                                          onChange={(e) => setComment(e.target.value)} />
                                <span className="mt-2" style={{ maxWidth: 480, fontSize: 14, color: '#999' }}>Расскажите информацию о заказе, подъезд, укажите код домофона или другую информацию, которая может пригодиться курьеру.</span>
                            </div>


                            <label className="mt-4 f">Тип доставки</label>
                            {weight > transportWeight && <div className="overloadMessage"><strong>Перегруз!</strong></div>}
                            <div className="d-flex gap-5">
                                {transportTypes.map((type) => (
                                  <div key={type} className={`deliveryType ${selectedType === type && 'selected'}`}
                                       onClick={() => setSelectedType(type)}>
                                      {getIconForTransportType(type)}
                                      {type}
                                  </div>
                                ))}
                            </div>

                            {number.number === '' && <div className="telNumber">
                                <strong>Телефон</strong>
                                <div style={{ fontSize: 14 }}>Подтвердите ваш номер телефона, на него будет отправлено
                                    SMS с кодом !
                                </div>
                                <div className="d-flex mt-2">
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
                                          type="button"
                                          className="confirmTel"
                                          disabled={!value}
                                          onClick={submitInnerForm}
                                        >
                                            Подтвердить номер
                                        </button>
                                    </form>
                                </div>
                                <div style={{ color: '#999', fontSize: 14 }} className="mt-1">Например +375 (29)
                                    842-05-07
                                </div>
                            </div>}
                            {calculateShippingCost() && <div className='mt-3'>
                                <strong>Итого: </strong>{calculateShippingCost()} р.
                            </div>}
                            <button className={`orderSentButton ${weight > transportWeight && 'disabled'}`}
                                    type="submit" disabled={weight > transportWeight}>
                                Заказать
                            </button>
                        </div>

                        <div>
                            <div>
                                Куда доставить товар
                                <div className="d-flex align-items-center">
                                <input className='Location' type="text" placeholder='Введите пункт доставки'
                                           defaultValue={deliveryPoint} required
                                           onChange={(e) => setDeliveryPoint(e.target.value)} />
                                    <input type='reset' className='resetLocationButton' value='x'
                                           onClick={() => {
                                               setDeliveryPoint(null);
                                               setDistance(null);
                                           }}/>
                                </div>
                                {isDeliverySearching ? (
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
                            </div>

                        </div>
                    </div>

                </form>
            </div>

            <ConfirmTel show={confirmVisible} onHide={() => setConfirmVisible(false)} />
            <OrderCheck show={visible} onHide={() => setVisible(false)} />
        </Container>
    );
});

export default DoOrder;

