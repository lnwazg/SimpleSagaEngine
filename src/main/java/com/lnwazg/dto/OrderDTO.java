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
    private BigDecimal payAmount;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
}
