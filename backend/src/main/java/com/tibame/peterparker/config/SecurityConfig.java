package com.tibame.peterparker.config;

import java.util.List;

import com.tibame.peterparker.service.OwnerService;
import com.tibame.peterparker.service.AdminUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.tibame.peterparker.filter.AuthRequestFilter;

@Configuration // 配置類
@EnableWebSecurity // 啟用 Spring Security
public class SecurityConfig {

    @Autowired
    private AuthRequestFilter authRequestFilter;

    @Autowired
    @Lazy
    private AdminUserDetailsService adminUserDetailsService;

    @Autowired
    @Lazy
    private OwnerService ownerService;

    // 密碼編碼器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Admin 身份驗證提供者
    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Owner 身份驗證提供者
    @Bean
    public DaoAuthenticationProvider ownerAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(ownerService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Admin AuthenticationManager 實例
    @Bean(name = "adminAuthenticationManager")
    @Primary
    public AuthenticationManager adminAuthenticationManager() {
        return new ProviderManager(List.of(adminAuthenticationProvider()));
    }

    // Owner AuthenticationManager 實例
    @Bean(name = "ownerAuthenticationManager")
    public AuthenticationManager ownerAuthenticationManager() {
        return new ProviderManager(List.of(ownerAuthenticationProvider()));
    }

    // 跨域請求設定
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:8081", "http://localhost:5500", "http://127.0.0.1:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 增加以確保包含 OPTIONS 方法
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 安全過濾鏈
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 啟用 CORS
        http.cors().configurationSource(corsConfigurationSource()) // 明確指定 CORS 配置來源
                .and().csrf().disable(); // 禁用 CSRF

        // 設定驗證的提供者
        http.authenticationProvider(adminAuthenticationProvider())
                .authenticationProvider(ownerAuthenticationProvider())
                .addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // 控制 api 使用權限
        http.authorizeRequests(authorize -> {
            authorize.antMatchers("/adminlogin", "/user/**", "/order/**", "/owner/**").permitAll();
            authorize.antMatchers("/api/admin/**", "/api/user/**", "/api/orders/**", "/api/owner/**", "/api/statistics/**", "/api/parking/**").permitAll();
            authorize.antMatchers("/official/**").permitAll();
            authorize.antMatchers("/geocode","/qr/**").permitAll();
            authorize.antMatchers("/api/**").hasAnyRole("ADMIN");
            authorize.anyRequest().authenticated();
        });

        // 設置會話策略為無狀態（使用 JWT 時推薦的配置）
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
