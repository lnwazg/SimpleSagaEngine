package com.lnwazg.service;

import com.lnwazg.dto.OrderDTO;
import com.lnwazg.dto.request.OrderCommitReqDTO;

public interface OrderService {
    OrderDTO createOrder(OrderCommitReqDTO orderCommitReqDTO);
}
