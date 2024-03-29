import React, { useState, useEffect } from 'react';
import { Form, Spinner, Image } from 'react-bootstrap';
import { fetchItemImage, searchItems } from "../http/itemAPI";
import { useNavigate } from "react-router-dom";

import "../styles/Search.css";

const SearchForm = () => {
    const navigate = useNavigate();
    const [query, setQuery] = useState('');
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(false);

    const ImageSearch=({imageName})=>{
        const [img, setImg] = useState("");

        useEffect(() => {
            fetchItemImage(imageName)
                .then((data) => {
                    setImg(URL.createObjectURL(data));
                })
                .catch((error) => {
                    console.error('Error fetching item image:', error);
                });
        }, [imageName]);
        return (<Image className="searchImg" src={img}/>)
    }

    useEffect(() => {
        let timeoutId;

        const fetchItems = async () => {
            try {
                setLoading(true);
                searchItems(query).then((data) => setItems(data));
                setLoading(false);
            } catch (error) {
                console.error(error);
                setLoading(false);
            }
        };

        const delayedSearch = () => {
            clearTimeout(timeoutId);
            timeoutId = setTimeout(fetchItems, 500); // Задержка в 500 миллисекунд
        };

        if (query !== '') {
            delayedSearch();
        } else {
            setItems([]);
        }

        return () => {
            clearTimeout(timeoutId);
        };
    }, [query]);

    const handleItemClick = (itemId) => {
        setQuery('');
        setItems([]);
    };

    return (
        <div>
            <Form style={{ marginLeft: 12 }}>
                <Form.Control
                    type="text"
                    placeholder="Введите поисковой запрос 🔎︎"
                    value={query}
                    style={{ width: 250 }}
                    onChange={(e) => setQuery(e.target.value)}
                />
            </Form>

            {/* Отображение найденных товаров или анимации ожидания */}
            <div className="resultContainer">
                {loading ? (
                    <Spinner animation="border" role="status">
                        <span className="visually-hidden">Загрузка...</span>
                    </Spinner>
                ) : (
                    items.map((item) => (
                        <div
                            key={item.id}
                            className='searchItem'
                            onClick={() => handleItemClick(item.id)}
                        >
                            <ImageSearch imageName={item.img}/>
                            {item.name} {item.price} p.
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};

export default SearchForm;