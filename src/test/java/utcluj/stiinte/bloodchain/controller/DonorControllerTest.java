package utcluj.stiinte.bloodchain.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcluj.stiinte.bloodchain.service.DonorService;
import utcluj.stiinte.bloodchain.service.TransfusionCenterService;
import utcluj.stiinte.bloodchain.service.authentication.JwtService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = DonorController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class DonorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private DonorService donorService;
    @MockitoBean
    private TransfusionCenterService transfusionCenterService;
    @MockitoBean
    private JwtService jwtService;
    
    @Test
    @DisplayName("Should throw exception if time contains letters")
    void testScheduleAppointment1() throws Exception {
        mockMvc.perform(post("/v1/donors/1/appointment")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "userId": 1,
                    "transfusionCenterId": 1,
                    "time": "abcd"
                }
                
                """)).andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Should return ok status")
    void testScheduleAppointment2() throws Exception {
        mockMvc.perform(post("/v1/donors/1/appointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                .content("""
                {
                    "userId": 1,
                    "transfusionCenterId": 1,
                    "time": "2025-10-10T10:00"
                }
                
                """)).andExpect(status().isOk());
    }
}