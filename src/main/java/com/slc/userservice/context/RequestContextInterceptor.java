package com.slc.userservice.context;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestContextInterceptor implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        try {
            String actor = request.getHeader("X-User-Email");
            String source = request.getHeader("X-Client-Source");
            String role = request.getHeader("X-User-Role");

            RequestContext.set(actor, source, role);
            chain.doFilter(req, res);
        } finally {
            RequestContext.clear();
        }
    }
}
