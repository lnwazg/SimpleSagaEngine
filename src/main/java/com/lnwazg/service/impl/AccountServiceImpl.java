package com.lnwazg.service.impl;

import com.lnwazg.dto.SaleInfoDTO;
import com.lnwazg.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Override
    public SaleInfoDTO querySaleInfo(Long saleAccountId) {
        return SaleInfoDTO.builder()
                .accountId(saleAccountId)
                .accountName("Apple旗舰店客服专用账号")
                .build();
    }
}
