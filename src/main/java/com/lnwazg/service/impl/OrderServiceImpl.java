package com.lnwazg.service.impl;

import com.lnwazg.dto.OrderDTO;
import com.lnwazg.dto.request.OrderCommitReqDTO;
import com.lnwazg.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public OrderDTO createOrder(OrderCommitReqDTO orderCommitReqDTO) {
        return OrderDTO.builder()
                .orderId(orderCommitReqDTO.getOrderId())
                .saleAccountId(orderCommitReqDTO.getSaleAccountId())
                .payAmount(orderCommitReqDTO.getPayAmount())
                .orderStatus(10)
                .createdTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .build();
    }
}
