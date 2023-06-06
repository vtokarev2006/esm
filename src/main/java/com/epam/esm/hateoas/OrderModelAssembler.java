package com.epam.esm.hateoas;

import com.epam.esm.controllers.OrderController;
import com.epam.esm.domain.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class OrderModelAssembler extends RepresentationModelAssemblerSupport<Order, OrderModel> {

    public OrderModelAssembler() {
        super(OrderController.class, OrderModel.class);
    }

    @Override
    public OrderModel toModel(Order order) {
        OrderModel orderModel = new OrderModel();
        BeanUtils.copyProperties(order, orderModel);
        return orderModel;
    }
}


