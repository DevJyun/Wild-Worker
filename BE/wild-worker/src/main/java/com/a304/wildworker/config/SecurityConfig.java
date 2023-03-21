package com.a304.wildworker.config;

import com.a304.wildworker.config.service.CustomLogoutHandler;
import com.a304.wildworker.config.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLogoutHandler logoutHandler;
    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   //TODO. csrf disable 안 하고 처리
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeHttpRequests()
                .antMatchers("/auth/login", "/oauth2/**").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(
                        HttpStatus.OK))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .formLogin().disable()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2UserService);

        return http.build();
    }
}