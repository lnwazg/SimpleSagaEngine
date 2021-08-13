package com.lnwazg.service.impl;

import com.lnwazg.dto.OrderDTO;
import com.lnwazg.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public OrderDTO createOrder() {
        return OrderDTO.builder()
                .orderId(new Random().nextLong())
                .saleAccountId(new Random().nextLong())
                .payAmount(new BigDecimal(1000))
                .createdTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .build();
    }
}
