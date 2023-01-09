package com.synrgy.security.configuration;

import com.synrgy.security.entity.enumeration.EnumRole;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

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
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                    .antMatchers("/","/showFile/**","/v1/showFile/**","/v1/upload", "/register-opt/**", "/register/**","/swagger-ui/**","/swagger-ui.html","/api/oauth/token**" ,"/v3/api-docs/**",
                            "/forget-password/**", "/oauth/authorize**", "/oauth/token**", "/login-user", "/logout**", "/error**")
                    .permitAll()
                .antMatchers("/v1/role-test-global/example").hasAnyAuthority(EnumRole.ROLE_READ.name())
                .and()
                .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                    .permitAll();
        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))

                );
    }
}
