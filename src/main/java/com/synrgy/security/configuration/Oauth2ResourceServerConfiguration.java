package com.synrgy.security.configuration;

import com.synrgy.security.entity.enumeration.EnumRole;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true) //secure definition
public class Oauth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * Manage resource server.
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }
//    private static final String SECURED_PATTERN = "/api/**";

    /**
     * Manage endpoints.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "" +
                                "/showFile/**", "/v1/showFile/**", "/v1/upload", "/register-opt/**", "/register/**", "/swagger-ui/**", "/api/swagger-ui.html", "/api/oauth/token**", "/api/oauth/token", "/v3/api-docs/**",
                        "/forget-password/**", "/oauth2/**", "/oauth/token", "/oauth/token**", "/login-user", "/logout**", "/error**", "/auth/**", "/error", "/api/webjars/*", "/api/user", "/api/login-oauth2-google", "/api/index.html")
                .permitAll()
                .and()
                .oauth2Login()
                .and()
//                implement logout
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .and()
                .authorizeRequests()
                .antMatchers("/api/seeker").hasAnyAuthority(com.synrgy.security.entity.enumeration.EnumRole.ROLE_SK.toString())
                .antMatchers("/api/tennant").hasAnyAuthority(com.synrgy.security.entity.enumeration.EnumRole.ROLE_TN.toString())
                .antMatchers("/api/superadmin").hasAnyAuthority(com.synrgy.security.entity.enumeration.EnumRole.ROLE_SUPERUSER.toString())
                .anyRequest().permitAll();
    }
}


