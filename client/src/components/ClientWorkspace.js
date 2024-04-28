import React, { useContext, useEffect, useState } from 'react'
import Rating from './modals/Rating'
import { Context } from '../index'
import { getAverageRating, getPartners, createRating, fetchRating, deleteRating } from '../http/partnerAPI'
import { observer } from 'mobx-react-lite'

import "../styles/ClientWorkspace.css"
import { ORDER_ROUTE } from '../utils/consts'
import { NavLink } from 'react-router-dom'

const ClientWorkspace = observer(() => {

  const { user, partners } = useContext(Context);
  const [average, setAverage] = useState(0);
  const [rating, setRating] = useState([])

  useEffect(() => {
    if(user.Auth){
      getPartners().then((partnersData) => {
        partners.setPartners(partnersData);
      });
    }
  }, []);

  useEffect(() => {
    if (partners.selectedPartner.id) {
      fetchRating(partners.selectedPartner.id).then((rate) => setRating(rate))
    }
  }, [partners.selectedPartner.id])

  useEffect(() => {
    if (partners.selectedPartner.id) {
      getAverageRating(partners.selectedPartner.id).then((avg) => setAverage(avg));
    }
  }, [partners.selectedPartner.id]);

  const CreateRating = (rate, feedback) => {
    createRating(partners.selectedPartner.id, rate, feedback).then(() => {
      fetchRating(partners.selectedPartner.id).then((rate) => {
        setRating(rate);
        getAverageRating(partners.selectedPartner.id).then((avg) => setAverage(avg));
      });
    });
  };

  const DeleteRating = () => {
    deleteRating(partners.selectedPartner.id).then(() => {
      fetchRating(partners.selectedPartner.id).then((rate) => {
        setRating(rate);
        getAverageRating(partners.selectedPartner.id).then((avg) => setAverage(avg));
      });
    });
  };

  console.log(partners.partners)
  return (
    <div className="main-page-container">
      <div className="d-flex p-1">
        <div className="partner-list">
          <h4>Наши партнёны</h4>
          {partners.partners &&
            partners.partners.map((partner) => {
              if (!partner.margin || !partner.companyOfficial) {
                return null;
              }
              return (
                <div
                  key={partner.id}
                  className="partner-item"
                  autoFocus={partner.id === partners.selectedPartner?.id}
                  onClick={() => partners.setPartner(partner)}
                >
                  <p>{partner.companyName}</p>
                  <p>email: {partner.email}</p>
                  <p>Телефон: {partner.contactNumber}</p>
                </div>
              );
            })}
        </div>
        <div className="partner-info">
          {partners.selectedPartner.id !== undefined ?
            <div>
              <p>INN: {partners.selectedPartner.inn}</p>
              <p>Название компании: {partners.selectedPartner.companyName}</p>
              <p>Email: {partners.selectedPartner.email}</p>
              <p>Контактный телефон: {partners.selectedPartner.contactNumber}</p>
              <p>Mаржа: {partners.selectedPartner.margin} %</p>
              <p>Представитель комапнии: {partners.selectedPartner.companyOfficial}</p>
              <p>Описание компании: {partners.selectedPartner.description}</p>
              <div>
                <div className="d-flex">
                  <div className="mark_rate">
                    {average}
                  </div>
                  <div className="rate"></div>
                  <div id="after_feedback"></div>
                  {user.Auth && <div
                    id="feedback-button"
                    onClick={() => {
                      const container =
                        document.getElementById('feedback-container')
                      container.setAttribute('style', 'display: block;')
                      const feedback =
                        document.getElementById('feedback-button')
                      feedback.setAttribute('style', 'display: none;')
                    }}
                  >
                    Оставить отзыв
                  </div>}

                </div>

                <div id="feedback-container">
                  <form>
                    <div className="d-flex mb-2">
                      <div className="rate_check">Ваша оценка:</div>
                      <div className="rating_block">
                        <input
                          name="rating"
                          value="5"
                          id="rating_5"
                          type="radio"
                        />
                        <label
                          htmlFor="rating_5"
                          className="label_rating"
                        ></label>

                        <input
                          name="rating"
                          value="4"
                          id="rating_4"
                          type="radio"
                        />
                        <label
                          htmlFor="rating_4"
                          className="label_rating"
                        ></label>

                        <input
                          name="rating"
                          value="3"
                          id="rating_3"
                          type="radio"
                        />
                        <label
                          htmlFor="rating_3"
                          className="label_rating"
                        ></label>

                        <input
                          name="rating"
                          value="2"
                          id="rating_2"
                          type="radio"
                        />
                        <label
                          htmlFor="rating_2"
                          className="label_rating"
                        ></label>

                        <input
                          name="rating"
                          value="1"
                          id="rating_1"
                          type="radio"
                        />
                        <label
                          htmlFor="rating_1"
                          className="label_rating"
                        ></label>
                      </div>
                    </div>

                    <textarea cols="40" rows="4" name="feedback"></textarea>

                    <div className="button_container">
                      <input
                        type="button"
                        value="Отправить"
                        onClick={() => {
                          let rating = ''
                          const textarea =
                            document.getElementsByName('feedback')[0]
                          if (textarea.value) {
                            const container =
                              document.getElementById('feedback-container')
                            container.setAttribute('style', 'display: none;')
                            const rate = document.getElementsByName('rating')
                            for (let i of rate) {
                              if (i.checked) {
                                rating = i.value
                                console.log(i.value)
                              }
                            }
                            const feedback =
                              document.getElementById('feedback-button')
                            feedback.setAttribute('style', 'display: none;')
                            const after =
                              document.getElementById('after_feedback')
                            after.textContent = 'Спасибо за отзыв !'
                            console.log(textarea.value)
                            CreateRating(rating, textarea.value)
                          }
                        }}
                      ></input>
                      <input type="reset"></input>
                      <input
                        type="reset"
                        value="Отмена"
                        onClick={() => {
                          const container =
                            document.getElementById('feedback-container')
                          container.setAttribute('style', 'display: none;')
                          const feedback =
                            document.getElementById('feedback-button')
                          feedback.setAttribute('style', 'display: block;')
                        }}
                      >
                      </input>
                    </div>
                  </form>
                </div>
              </div>

              <div>
                <button><NavLink to={ORDER_ROUTE}>Cделать заказ</NavLink></button>
              </div>


              <div id="feedback" >
                {rating.length === 0 ? (
                  <div className="no-feedback d-flex justify-content-center">Отзывов на данного поставщика пока нет
                    😒</div>) : (
                  rating.map((info) => (
                    <div key={info.id} className="feedback_about_item">
                      <div className='d-flex justify-content-between'>
                        <Rating rating={info.rate} />
                          {user.user.id === info.client.id &&
                            <div className="deleteRate" onClick={() => DeleteRating(partners.selectedPartner.id)}>Delete comment</div>
                          }
                      </div>

                      <div>{info.client.email} : {info.feedback}</div>
                    </div>
                  )))
                }
              </div>
            </div>
            :
            <div>No partner is Selected</div>
          }
        </div>
      </div>
    </div>
  )
})

export default ClientWorkspace