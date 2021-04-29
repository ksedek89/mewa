package com.pru.mewa.security.config;

import com.pru.cas.sso.auth.mapper.AuthenticationMapper;
import com.pru.cas.sso.config.SsoWebSecurityConfig;
import com.pru.cas.sso.filter.SessionCheckerFilter;
import com.pru.cas.sso.provider.auth.AuthenticationProviderClient;
import com.pru.cas.sso.provider.session.SessionManagerClient;
import com.pru.cas.sso.session.mapper.SessionMapper;
import com.pru.cas.sso.session.strategy.SessionInformationExpiredStrategy;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@EnableSpringHttpSession
public class WebSecurityConfig extends SsoWebSecurityConfig {

    private static final String[] AUTHORIZED_PATTERNS = Stream.of(
        "/api/version",
        "/api/translations/**",
        "/api/log/**",
        "/version",
        "/actuator/**"
    ).toArray(String[]::new);

    public WebSecurityConfig(
        AuthenticationEntryPoint authenticationEntryPoint,
        SessionInformationExpiredStrategy sessionInformationExpiredStrategy,
        AuthenticationMapper authenticationMapper,
        AuthenticationProviderClient authenticationProviderClient,
        SessionMapper sessionMapper,
        SessionManagerClient sessionManagerClient
    ) {
        super(
            authenticationEntryPoint,
            sessionInformationExpiredStrategy,
            authenticationMapper,
            authenticationProviderClient,
            sessionMapper,
            sessionManagerClient
        );
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/**",
            "/swagger-ui.html",
            "/webjars/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
            .authorizeRequests()
            .antMatchers(AUTHORIZED_PATTERNS).permitAll()
            .antMatchers("/api/**").authenticated()
        .and()
            .addFilterAfter(sessionOneTabFilter(), SessionCheckerFilter.class)
        ;
    }

    @Override
    protected String[] getAuthorizedPatterns() {
        return Stream.concat(Stream.of(AUTHORIZED_PATTERNS), Stream.of("/api/auth/init", "/api/auth/invalidateothersessions")).toArray(String[]::new);
    }
}
