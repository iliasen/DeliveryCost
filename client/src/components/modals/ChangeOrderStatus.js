import React, { useState } from 'react'
import { observer } from 'mobx-react-lite'
import Modal from 'react-bootstrap/Modal'
import { Button, Form } from 'react-bootstrap'
import { changeStatus } from '../../http/orderAPI'

const ChangeOrderStatus = observer(({ show, onHide, orderId}) => {
  const [selectedStatus, setSelectedStatus] = useState('');
  const statusOptions = [
    { value: 'WITHOUT', label: 'Без статуса' },
    { value: 'NEW', label: 'Новый' },
    { value: 'ADOPTED', label: 'Принят' },
    { value: 'DELIVERED', label: 'Доставлен' },
    { value: 'CANCELLED', label: 'Отменён' },
    { value: 'COMPLETE', label: 'Завершен' }
  ];

  const handleStatusChange = (status) => {
    setSelectedStatus(status);
  };

  const handleSaveStatus = () => {
    changeStatus(orderId, selectedStatus).then((r) => onHide())
    // window.location.reload()
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton />
      <Modal.Body className="locationContainer text-center">
        <div className="map" />
        <Form.Text className="wereAreYou">Выберите статус:</Form.Text>
        <Form.Control
          as="select"
          value={selectedStatus}
          onChange={(e) => handleStatusChange(e.target.value)}
        >
          {statusOptions.map((status) => (
            <option key={status.value} value={status.value}>
              {status.label}
            </option>
          ))}
        </Form.Control>
      </Modal.Body>
      <Modal.Footer className="d-flex justify-content-center">
        <Button variant="outline-success" style={{ width: 300 }} onClick={handleSaveStatus}>
          Изменить
        </Button>
      </Modal.Footer>
    </Modal>
  );
});

export default ChangeOrderStatus;