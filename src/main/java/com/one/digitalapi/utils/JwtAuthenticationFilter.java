package com.one.digitalapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.digitalapi.entity.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Skip JWT authentication for permitted endpoints
        if (requestURI.startsWith("/auth") ||
                requestURI.startsWith("/digital/api/swagger-ui") ||
                requestURI.startsWith("/digital/api/v3/api-docs") ||
                requestURI.startsWith("/digital/api/swagger-ui/index.html") ||
                requestURI.startsWith("/digital/api/auth/check-user/") ||
                requestURI.equals("/digital/api/error") ||
                requestURI.equals("/digital/api/auth/login") ||
                requestURI.equals("/digital/api/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            handleUnauthorizedError(response, request, "Token not provided or invalid token.");
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        String username = null;

        try {
            username = jwtUtil.extractUsername(token);
        } catch (ExpiredJwtException e) {
            handleUnauthorizedError(response, request, "Token has expired. Please log in again.");
            return;
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            handleUnauthorizedError(response, request, "Invalid token.");
            return;
        }

        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (username == null || (existingAuth != null && existingAuth.isAuthenticated())) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.isTokenValid(token, userDetails.getUsername())) {
            handleUnauthorizedError(response, request, "Invalid token.");
            return;
        }

        // Cast to CustomUserDetails so we can extract admin flag (if needed)
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails,
                        null,
                        customUserDetails.getAuthorities()
                );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }


    private void handleUnauthorizedError(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("error", message);
        errorResponse.put("path", request.getRequestURI());

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}