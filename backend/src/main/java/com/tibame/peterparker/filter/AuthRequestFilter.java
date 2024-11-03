package com.tibame.peterparker.filter;

import com.tibame.peterparker.service.AdminUserDetailsService;
import com.tibame.peterparker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class AuthRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private AdminUserDetailsService userDetailsService;

    private final RequestMatcher ignoredPaths = new AntPathRequestMatcher("/**/user/**");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        boolean result =this.ignoredPaths.matches(request);
        return result;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

            // 從請求的 cookie 中找出名為 "jwtToken" 的 cookie
           // Cookie[] cookies = request.getCookies();


            String jwtHeader = request.getHeader("Authorization");
            if(!jwtHeader.contains("Bearer"))
                throw new BadCredentialsException("Invalid JWT token");

            String jwt = jwtHeader.split(" ")[1];

            // 如果找到了 JWT，則進行驗證
            if (!(jwt.isEmpty() || jwt.isBlank())) {
                String username = jwtUtil.extractUsername(jwt);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        // 如果驗證成功，設置 Spring Security 上下文
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }

        chain.doFilter(request, response);
    }
}
