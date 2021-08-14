package com.lnwazg.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderDTO {
    private Long orderId;
    private Long saleAccountId;
    //10初始化 20待支付 30已支付
    private Integer orderStatus;
    private BigDecimal payAmount;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;

}
