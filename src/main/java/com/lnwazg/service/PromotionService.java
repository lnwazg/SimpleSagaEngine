package com.lnwazg.service;

import com.lnwazg.dto.request.OrderCommitReqDTO;

public interface PromotionService {
    void calcPrice(OrderCommitReqDTO orderCommitReqDTO);
}
