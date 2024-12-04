import React, { useEffect, useState } from 'react'
import Modal from 'react-bootstrap/Modal'
import { Button, Dropdown, Form } from 'react-bootstrap'
import { observer } from 'mobx-react-lite'
import { addTransport, getFreeDrivers } from '../../http/driverAPI'

const AddDriver = observer(({ show, onHide, transport }) => {

  const [drivers, setDrivers] = useState([])
  const [selectedDriver, setSelectedDriver] = useState('')

  useEffect(() => {
    getFreeDrivers().then(data => setDrivers(data))
  }, [])

  console.log(drivers)
  console.log(transport)
  const addTransportToDriver = () => {
    // console.log(item.selectedBrand.id, value)
    // changeBrand(item.selectedBrand.id, value, country).then(data =>  {onHide()})
    addTransport(selectedDriver, transport).then(r => {
      onHide()
    })
    console.log(transport)
  }

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          Добавление водителя
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <div>
            <Form.Text>Водители</Form.Text>

            {drivers.length != 0 ? <>{drivers.map(driver =>
              <Dropdown className="mt-2 mb-2">
                <Dropdown.Toggle
                  variant="outline-dark">{selectedDriver.firstName || 'Выберите водителя'}</Dropdown.Toggle>
                <Dropdown.Menu>
                  <Dropdown.Item
                    onClick={() => setSelectedDriver(driver)}
                    key={driver.id}
                  >
                    {driver.firstName} {driver.lastName}
                  </Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>,
            )}</> :
              <div>Свободных водителей нет, наймите водителя !</div>
            }


          </div>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        {drivers.length != 0 &&
        <Button variant="outline-success" onClick={addTransportToDriver}>
          Подтвердить
        </Button>}
      </Modal.Footer>
    </Modal>
  )
})

export default AddDriver
