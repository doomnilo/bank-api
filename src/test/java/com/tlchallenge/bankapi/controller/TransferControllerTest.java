package com.tlchallenge.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlchallenge.bankapi.model.Transfer;
import com.tlchallenge.bankapi.model.dto.TransferDto;
import com.tlchallenge.bankapi.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testGetTransfer_found() throws Exception {
        when(transferService.getTransferById(1L)).thenReturn(Optional.of(transfer1));

        mockMvc.perform(get("/api/v1/transfers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fromAccountId").value(1L))
                .andExpect(jsonPath("$.toAccountId").value(2L))
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testGetTransfer_notFound() throws Exception {
        when(transferService.getTransferById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/transfers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTransfer_success() throws Exception {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(BigDecimal.valueOf(100));

        when(transferService.createTransfer(any(TransferDto.class))).thenReturn(transfer1);

        mockMvc.perform(post("/api/v1/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fromAccountId").value(1L))
                .andExpect(jsonPath("$.toAccountId").value(2L))
                .andExpect(jsonPath("$.amount").value(100))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testGetAllTransfers_success() throws Exception {
        List<Transfer> transfers = Arrays.asList(transfer1, transfer2);
        when(transferService.getAllTransfers()).thenReturn(transfers);

        mockMvc.perform(get("/api/v1/transfers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void testGetAllTransfers_emptyList() throws Exception {
        when(transferService.getAllTransfers()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/transfers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(0));
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