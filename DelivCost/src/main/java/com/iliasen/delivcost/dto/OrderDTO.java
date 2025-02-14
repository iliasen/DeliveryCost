package com.iliasen.delivcost.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private Long id;
    private String route;
    private boolean partnerChecked;
    private boolean clientSubscribe;
    private String comment;
    private int price;
    private String orderStatus;
    private CargoDTO cargo;
}


