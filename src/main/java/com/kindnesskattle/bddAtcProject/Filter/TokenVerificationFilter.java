package com.kindnesskattle.bddAtcProject.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class TokenVerificationFilter extends GenericFilterBean {

    private JwtDecoder jwtDecoder;

    public void setJwtDecoder(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            httpResponse.setStatus(HttpStatus.OK.value());
            return;
        }

        // Allow requests to certain endpoints to bypass token verification
        if (isPublicEndpoint(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header
        String token = extractToken(httpRequest.getHeader("Authorization"));
        if (token != null) {
            try {
                // Parse and verify the token
                Jwt decodedJwt = jwtDecoder.decode(token);

                // Proceed with the filter chain
                chain.doFilter(request, response);

            } catch (JwtException e) {
                // Token verification failed
                httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpResponse.getWriter().write("Invalid token: " + e.getMessage());
                return;
            }
        } else {
            // Token is missing
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("Token is missing");
            return;
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest httpRequest) {
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        return ("/login/auth".equals(uri) && "POST".equals(method)) ||
                ("/cities/getAllUserCities".equals(uri) && "GET".equals(method)) ||
                ("/register".equals(uri) && "POST".equals(method));
    }

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}