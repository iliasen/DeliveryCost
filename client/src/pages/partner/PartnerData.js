import React, { useState } from 'react'
import { Button, Form } from 'react-bootstrap'
import { updateFields } from '../../http/partnerAPI'

const PartnerData = () => {
  const [companyOfficial, setCompanyOfficial] = useState('');
  const [description, setDescription] = useState('');
  const [margin, setMargin] = useState(0);
  const [file, setFile] = useState(null)

  const handleFormReset = () => {
    setCompanyOfficial('');
    setDescription('');
    setMargin(0);
    setFile(null);
  };

  const updatePartnerFields = () => {
    const formData = new FormData();
    formData.append('companyOfficial', companyOfficial);
    formData.append('description', description);
    formData.append('margin', `${margin}`);
    formData.append('img', file);

    updateFields(formData).then();
  };

  const selectFile = e => {
    setFile(e.target.files[0])
  }
  return (
    <div  style={{ width: '100%', height: '100vh' }} >
      <h1>Заполните свой профиль прежде чем начать</h1>
      <Form>
        <Form.Group controlId="companyOfficial">
          <Form.Label>Ваш представитель комапнии:</Form.Label>
          <Form.Control
            type="text"
            value={companyOfficial}
            onChange={(e)=>setCompanyOfficial(e.target.value)}
          />
        </Form.Group>
        <Form.Group controlId="description">
          <Form.Label>Краткое описание компании:</Form.Label>
          <Form.Control
            required
            type="text"
            value={description}
            onChange={e=>setDescription(e.target.value)}
          />
        </Form.Group>
        <Form.Group controlId="margin">
          <Form.Label>Ваша маржа(%):</Form.Label>
          <Form.Control
            required
            type="number"
            value={margin}
            onChange={e => setMargin(e.target.value)}
          />
        </Form.Group>
        <Form.Group controlId="companyLogo">
          <Form.Label>Выберите логотип компании:</Form.Label>
          <Form.Control
            required
            type="file"
            onChange={selectFile}
          />
        </Form.Group>
        <Button variant="primary" type="button" onClick={updatePartnerFields}>
          Отправить
        </Button>
        <Button variant="dark" type="submit" onClick={handleFormReset}>
          Сбросить занчения
        </Button>
      </Form>
    </div>
  );
};

export default PartnerData;
