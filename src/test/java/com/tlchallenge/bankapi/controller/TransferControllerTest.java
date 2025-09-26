package com.tlchallenge.bankapi.controller;

import com.tlchallenge.bankapi.model.Transfer;
import com.tlchallenge.bankapi.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    private Transfer transfer1;
    private Transfer transfer2;

    @BeforeEach
    void setUp() {
        transfer1 = new Transfer();
        transfer1.setId(1L);
        transfer1.setFromAccountId(1L);
        transfer1.setToAccountId(2L);
        transfer1.setAmount(BigDecimal.valueOf(100));
        transfer1.setTransferDate(LocalDateTime.now().minusHours(1));
        transfer1.setStatus("COMPLETED");

        transfer2 = new Transfer();
        transfer2.setId(2L);
        transfer2.setFromAccountId(1L);
        transfer2.setToAccountId(3L);
        transfer2.setAmount(BigDecimal.valueOf(200));
        transfer2.setTransferDate(LocalDateTime.now());
        transfer2.setStatus("COMPLETED");
    }

    @Test
    void getTransfer() {
    }

    @Test
    void createTransfer() {
    }

    @Test
    void getAllTransfers() {
    }

    @Test
    void testGetTransfersByFromAccountId_success() throws Exception {
        List<Transfer> transfers = Arrays.asList(transfer2, transfer1);
        when(transferService.getTransfersByFromAccountId(1L)).thenReturn(transfers);

        mockMvc.perform(get("/api/v1/transfers/account/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].fromAccountId").value(1L))
                .andExpect(jsonPath("$[0].toAccountId").value(3L))
                .andExpect(jsonPath("$[0].amount").value(200))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$[1].id").value(1L))
                .andExpect(jsonPath("$[1].fromAccountId").value(1L))
                .andExpect(jsonPath("$[1].toAccountId").value(2L))
                .andExpect(jsonPath("$[1].amount").value(100))
                .andExpect(jsonPath("$[1].status").value("COMPLETED"));
    }

    @Test
    void testGetTransfersByFromAccountId_emptyList() throws Exception {
        when(transferService.getTransfersByFromAccountId(99L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/transfers/account/99"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(0));
    }
}