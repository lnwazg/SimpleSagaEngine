package com.lnwazg.dto.request;

import com.lnwazg.dto.GoodsInfoDTO;
import com.lnwazg.dto.SaleInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderCommitReqDTO {
    private Long saleAccountId;
    private List<Long> skuIdList;
    private List<Integer> skuQuantityList;

    private List<GoodsInfoDTO> goodsInfoDTOS;
    private SaleInfoDTO saleInfoDTO;
    private BigDecimal payAmount;
    private Long orderId;
}
