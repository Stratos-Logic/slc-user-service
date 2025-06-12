package com.slc.userservice.controller;

import com.slc.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.MockedServiceConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private static UserService userService;

    static class MockedServiceConfig {
        @Bean
        public UserService userService() {
            return userService;
        }
    }

    @Test
    void testGetAllUsersReturnsOk() throws Exception {
        given(userService.getAllUsers()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }
}
