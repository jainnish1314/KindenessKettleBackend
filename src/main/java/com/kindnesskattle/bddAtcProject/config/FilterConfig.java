package com.kindnesskattle.bddAtcProject.config;


import com.kindnesskattle.bddAtcProject.Filter.TokenVerificationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TokenVerificationFilter> tokenVerificationFilter(JwtDecoder jwtDecoder) {
        FilterRegistrationBean<TokenVerificationFilter> registrationBean = new FilterRegistrationBean<>();

        TokenVerificationFilter filter = new TokenVerificationFilter();
        filter.setJwtDecoder(jwtDecoder);

        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return registrationBean;
    }
}