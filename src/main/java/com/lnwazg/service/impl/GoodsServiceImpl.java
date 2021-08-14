package com.lnwazg.service.impl;

import com.lnwazg.dto.GoodsInfoDTO;
import com.lnwazg.service.GoodsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Override
    public List<GoodsInfoDTO> queryGoodsInfo(List<Long> skuIdList) {
        return skuIdList.stream().map(x -> {
            if (x == 1) {
                return GoodsInfoDTO.builder().skuId(x).skuName("Apple/苹果 Mac mini Apple M1").skuPrice(new BigDecimal(6545.00)).build();
            } else if (x == 2) {
                return GoodsInfoDTO.builder().skuId(x).skuName("苹果 MacBook Air 13.3英寸M1笔记本电脑教育优惠").skuPrice(new BigDecimal(6660.00)).build();
            } else {
                return GoodsInfoDTO.builder().skuId(x).skuName("苹果 13 英寸 MacBook Pro Apple M1 芯片，配备 8 核中央处理器和 8 核图形处理器 512GB 存储容量").skuPrice(new BigDecimal(12745.00)).build();
            }
        }).collect(Collectors.toList());
    }
}
