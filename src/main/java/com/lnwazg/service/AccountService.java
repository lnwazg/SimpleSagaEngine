package com.lnwazg.service;

import com.lnwazg.dto.SaleInfoDTO;

public interface AccountService {
    SaleInfoDTO querySaleInfo(Long saleAccountId);
}
