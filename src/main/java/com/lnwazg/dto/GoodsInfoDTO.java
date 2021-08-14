package com.lnwazg.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GoodsInfoDTO {
    private Long skuId;
    private String skuName;
    private BigDecimal skuPrice;
}
