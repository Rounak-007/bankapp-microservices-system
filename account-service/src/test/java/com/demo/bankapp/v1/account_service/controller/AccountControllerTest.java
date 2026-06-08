package com.demo.bankapp.v1.account_service.controller;

import com.demo.bankapp.v1.account_service.configProperties.ConfigProperties;
import com.demo.bankapp.v1.account_service.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@ImportAutoConfiguration(classes = RefreshAutoConfiguration.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private ConfigProperties configProperties;

    @Test
    void welcomeMsg() throws Exception {
        mockMvc.perform(get("/api/accounts/welcome"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Github actions Demo of Account-Service"));
    }
}