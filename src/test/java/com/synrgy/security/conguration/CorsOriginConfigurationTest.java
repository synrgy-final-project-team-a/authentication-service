package com.synrgy.security.conguration;

import com.synrgy.security.configuration.CorsOriginConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CorsOriginConfigurationTest {

    private CorsOriginConfiguration corsFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    public void setUp() {
        corsFilter = new CorsOriginConfiguration();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    public void doFilter_ShouldAddCorsHeadersToResponse() throws ServletException, IOException {
        corsFilter.doFilter(request, response, filterChain);

        assertEquals("*", response.getHeader("Access-Control-Allow-Origin"));
        assertEquals("POST, GET, PUT, PATCH, OPTIONS, DELETE", response.getHeader("Access-Control-Allow-Methods"));
        assertEquals("3600", response.getHeader("Access-Control-Max-Age"));
        assertEquals("Origin, X-Requested-With, Content-Type, Accept, Authorization, Cache-Control, X-Auth-Token, Data", response.getHeader("Access-Control-Allow-Headers"));
    }

    @Test
    public void doFilter_ShouldReturnHttpOkWhenRequestMethodIsOptions() throws ServletException, IOException {
        request.setMethod("OPTIONS");

        corsFilter.doFilter(request, response, filterChain);

        assertEquals(200, response.getStatus());
    }
}


