import React from 'react'

import '../../styles/Payment_Guarantees.css'
const Payment = () => {
  return (
    <div className="payment-guarantees-back">
      <div className="payment-guarantees-container">
        <div className="context_margin">
          <h1>Оплата в интернет-магазине</h1>
          <div>
            Мы позаботились о том, чтобы оплата покупки была доступна в той
            форме, которую предпочитаете лично <strong>Вы</strong>. Независимо
            от выбранной формы оплаты, расчет производится только в белорусских
            рублях.
          </div>
          <p />

          <h3>Наличный расчет</h3>
          <ul>
            <li>
              Вы можете оплатить покупку при доставке, рассчитываясь с курьером
              наличными деньгами.
            </li>
            <li>
              Если вы осуществляете самовывоз, оплату необходимо будет совершить
              в кассе пункта выдачи товара.
            </li>
          </ul>
          <div style={{ borderTop: '1px solid black' }}></div>
          <p></p>

          <h3>Банковские карты</h3>
          <p>
            <strong>Оплата банковской картой через терминал</strong>
          </p>
          <ul>
            <li>
              Оплата курьеру при доставке. Сообщите выбранный способ оплаты во
              время заказа, и курьер приедет к вам с терминалом.
            </li>
            <li>
              Также возможна оплата через терминалы во всех пунктах самовывоза.
            </li>
          </ul>
          <p>Оплата банковской картой онлайн *</p>
          <ul>
            <li>
              Оплата банковской картой VISA, MasterCard, МИР, UnionPay, БЕЛКАРТ
              через систему Assist Belarus. Оплата производится через интернет в
              режиме реального времени непосредственно после оформления и
              подтверждения заказа.
            </li>
          </ul>
          <div style={{ borderTop: '1px solid black' }}></div>
          <p></p>

          <h3>По безналичному расчету (для юридических лиц)</h3>
          <p>
            Счет-фактура на оплату заказа приходит на e-mail или факс, указанный
            при регистрации и создании кабинета вашей организации на сайте
            Гефест
          </p>
          <div style={{ borderTop: '1px solid black' }}></div>
          <p></p>

          <h3>*Оплата банковской картой онлайн</h3>
          <p />
          <div>
            <img src="https://mile.by/upload/medialibrary/a73/visa.png" />
            <img src="https://mile.by/upload/medialibrary/834/master.png" />
            <img src="https://mile.by/upload/medialibrary/330/belkart.png" />
            <img src="https://mile.by/upload/IMG/oplata/unionpay.png" />
            <img src="https://mile.by/upload/IMG/oplata/mir_one.jpg" />
            <img src="https://mile.by/upload/medialibrary/ae0/assist.png" />
          </div>
          <p></p>
          <p>
            1. Оплата банковской картой VISA, MasterCard, UnionPay, БЕЛКАРТ
            через систему Assist Belarus. Оплата производится через интернет в
            режиме реального времени непосредственно после оформления заказа.
          </p>
          <p>
            2. Для совершения финансовой операции подходят карточки
            международных платежных систем VISA (всех видов), MasterCard (в том
            числе Maestro), эмитированные любым банком мира, МИР, UnionPay, а
            также карты платежной системы БЕЛКАРТ.
          </p>
          <p>
            При выборе оплаты заказа с помощью банковской карты, обработка
            платежа (включая ввод номера банковской карты) производится ООО
            «Компания Электронных Платежей «АССИСТ» с использованием
            программно-аппаратного комплекса системы электронных платежей Assist
            Belarus, которая прошла международную сертификацию.
          </p>
          <p>
            В системе, обеспечивающей безопасность платежей, используется
            защищённый протокол TLS для передачи конфиденциальной информации от
            клиента на сервер и дальнейшей обработки в процессинговом центре.
            Это значит, что конфиденциальные данные плательщика (реквизиты
            карты, регистрационные данные и др.) не поступают в
            интернет-магазин, их обработка полностью защищена, и никто не может
            получить персональные и банковские данные клиента. Кроме того, при
            обработке платежей по банковским картам, используется безопасная
            технология Visa Secure для VISA, MasterCard ID Check для MasterCard,
            Mir Accept для МИР, UnionPay 3-D Secure для UnionPay, а также
            БЕЛКАРТ – Интернет Пароль для БЕЛКАРТ.
          </p>
          {/*br*/}
          <p>
            При выборе оплаты заказа с помощью банковской карты, обработка
            платежа (включая ввод номера банковской карты) производится ООО
            «Компания Электронных Платежей «АССИСТ» с использованием
            программно-аппаратного комплекса системы электронных платежей Assist
            Belarus, которая прошла международную сертификацию.
          </p>
          <ol>
            <li>Выбрать способ оплаты картой on-line.</li>
            <li>
              После нажатия на кнопку «Подтвердить и оплатить» система направит
              вас на сайт провайдера электронных платежей belassist.by,
              обеспечивающего безопасность платежей. Авторизационный сервер
              устанавливает с покупателем соединение по защищённому протоколу
              TLS и принимает от покупателя параметры его банковской карты
              (номер карты, дата окончания действия карты, имя держателя карты в
              той транскрипции, как указано на банковской карте, а также номер
              CVC2 либо CVV2, указанные на обратной стороне карты). Операция
              оплаты банковской картой онлайн полностью конфиденциальна и
              безопасна.
            </li>
            <li>
              Ваши персональные данные и реквизиты карточки вводятся не на
              странице нашего сайта, а на авторизационной странице платежной
              системы. Доступ к этим данным осуществляется по протоколу
              безопасной передачи данных TLS, также применяются технологии
              безопасных интернет-платежей Visa Secure, Mastercard ID Check, Mir
              Accept, UnionPay 3-D Secure, БЕЛКАРТ-ИнтернетПароль.
            </li>
          </ol>

          <p>
            К оплате принимаются карты платежных систем Visa, MasterCard, МИР,
            UnionPay и БЕЛКАРТ, эмитированные любыми банками мира. Мы
            рекомендуем заранее обратиться в свой банк, чтобы удостовериться в
            том, что ваша карта может быть использована для платежей в сети
            интернет.
          </p>

          <p></p>
          <p></p>

          <p>
            <strong>Причины отказа в авторизации могут быть следующими:</strong>
          </p>
          <ul>
            <li>на карте недостаточно средств для оплаты заказа;</li>
            <li>
              банк, выпустивший карточку покупателя, установил запрет на оплату
              в интернете;
            </li>
            <li>истекло время ожидания ввода данных банковской карты;</li>
            <li>
              введённые данные не были подтверждены вами на платежной странице,
              ошибка формата данных и.т.д.
            </li>
          </ul>

          <p>
            <strong>
              В зависимости от причины отказа в авторизации для решения вопроса
              вы можете:
            </strong>
          </p>
          <ul>
            <li>
              обратиться за разъяснениями в банк, выпустивший карточку
              покупателя;
            </li>
            <li>
              в случае невозможности решения проблемы банком — повторить попытку
              оплаты, воспользовавшись картой, выпущенной другим банком.
            </li>
          </ul>
        </div>
      </div>
    </div>
  )
}
export default Payment
