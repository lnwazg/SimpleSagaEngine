package com.lnwazg.service;

import com.lnwazg.dto.GoodsInfoDTO;

import java.util.List;

public interface GoodsService {
    List<GoodsInfoDTO> queryGoodsInfo(List<Long> skuIdList);
}
