package com.lnwazg.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleInfoDTO {
    private Long accountId;
    private String accountName;
}
