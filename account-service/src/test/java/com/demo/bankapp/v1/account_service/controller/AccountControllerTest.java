package com.demo.bankapp.v1.account_service.controller;

import com.demo.bankapp.v1.account_service.configProperties.ConfigProperties;
import com.demo.bankapp.v1.account_service.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@ImportAutoConfiguration(classes = RefreshAutoConfiguration.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    void testHomeEndpoint_ShouldReturnCombinedMessage() throws Exception {
        // 1. Arrange: Define what the mocked config bean should return
        String mockMessage = "this is account service";
        when(configProperties.getMessage()).thenReturn(mockMessage);

        // 2. Act & Assert: Perform the GET request and verify the output
        mockMvc.perform(get("/api/accounts/home"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Dear Customer...." + mockMessage));
    }
}