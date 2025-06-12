package com.slc.userservice.context;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

public class RequestContextInterceptorTest {

    @AfterEach
    void tearDown() {
        RequestContext.clear();
    }

    @Test
    void testDoFilterSetsRequestContext() throws Exception {
        RequestContextInterceptor filter = new RequestContextInterceptor();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-User-Email", "user@example.com");
        request.addHeader("X-Client-Source", "ui");
        request.addHeader("X-User-Role", "user");

        MockHttpServletResponse response = new MockHttpServletResponse();

        MockFilterChain chain = new MockFilterChain() {
            @Override
            public void doFilter(ServletRequest req, ServletResponse res){
                assertEquals("user@example.com", RequestContext.getActor());
                assertEquals("ui", RequestContext.getSource());
                assertEquals("user", RequestContext.getRole());
            }
        };

        filter.doFilter(request, response, chain);
    }
}