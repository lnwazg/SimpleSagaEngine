package com.lnwazg.service.impl;

import com.lnwazg.dto.GoodsInfoDTO;
import com.lnwazg.dto.request.OrderCommitReqDTO;
import com.lnwazg.service.PromotionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Override
    public void calcPrice(OrderCommitReqDTO orderCommitReqDTO) {
        List<GoodsInfoDTO> goodsInfoDTOS = orderCommitReqDTO.getGoodsInfoDTOS();
        List<Integer> skuQuantityList = orderCommitReqDTO.getSkuQuantityList();
        BigDecimal payAmount = new BigDecimal(0);
        for (int i = 0; i < goodsInfoDTOS.size(); i++) {
            payAmount = payAmount.add(goodsInfoDTOS.get(i).getSkuPrice().multiply(new BigDecimal(skuQuantityList.get(i))));
        }
        orderCommitReqDTO.setPayAmount(payAmount);
    }
}
