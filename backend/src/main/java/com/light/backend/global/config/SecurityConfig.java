package com.light.backend.global.config;

import com.light.backend.global.jwt.JwtAuthenticationFilter;
import com.light.backend.global.jwt.JwtExceptionFilter;
import com.light.backend.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    private static final String MEMBER_PREFIX = "/members";
    private static final String SLOT_PREFIX = "/slots";

    private static final String MASTER = "master";
    private static final String ADMIN = "admin";
    private static final String MEMBER = "member";

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf((csrf) -> csrf.disable())
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {

                    //members
                    auth.requestMatchers(HttpMethod.POST, MEMBER_PREFIX).hasAnyAuthority(MASTER, ADMIN);
                    auth.requestMatchers(HttpMethod.GET, MEMBER_PREFIX).hasAnyAuthority(MASTER, ADMIN);
                    auth.requestMatchers(HttpMethod.POST, MEMBER_PREFIX + "/login").permitAll();
                    auth.requestMatchers(HttpMethod.GET, MEMBER_PREFIX + "/reissue").permitAll();

                    auth.requestMatchers(HttpMethod.GET, SLOT_PREFIX).hasAnyAuthority(MASTER, ADMIN, MEMBER);
                    auth.requestMatchers(HttpMethod.POST, SLOT_PREFIX).hasAnyAuthority(ADMIN);
                    auth.requestMatchers(HttpMethod.DELETE, SLOT_PREFIX).hasAnyAuthority(MASTER);
                    auth.requestMatchers(HttpMethod.PATCH, SLOT_PREFIX).hasAnyAuthority(MASTER, ADMIN, MEMBER);
                    auth.requestMatchers(HttpMethod.GET, SLOT_PREFIX+"/dashboard").hasAnyAuthority(MASTER, ADMIN, MEMBER);
                    
                })
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://light-slot.vercel.app");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Cookie");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
