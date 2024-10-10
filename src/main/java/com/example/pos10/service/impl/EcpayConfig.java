package com.example.pos10.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ecpay.payment.integration.AllInOne;

@Configuration
public class EcpayConfig {

	@Bean
    public AllInOne allInOne() {
        return new AllInOne(""); // 您可以根據需要傳入參數
    }
}
