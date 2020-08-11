package com.christophe.quoteService.configuration;

import com.christophe.quoteService.component.TokenAuthenticationFilter;
import com.christophe.quoteService.component.BCryptManager;
import com.christophe.quoteService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true, // (1)
        securedEnabled = true, // (2)
        jsr250Enabled = true)
public class GlobalSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Autowired
    TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    public GlobalSecurity(UserService userService) {
        this.userService = userService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(BCryptManager.passwordEncoder());
    }

    @Override
    public void configure (HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/auth").permitAll()
                    .antMatchers(HttpMethod.POST, "/user").permitAll()
                    .antMatchers(HttpMethod.GET,"/user").hasAuthority("USER")
                    .antMatchers(HttpMethod.GET,"/user/*").hasAnyAuthority("ADMIN","MANAGER")
                    .antMatchers(HttpMethod.PUT,"/user").hasAuthority("USER")
                    .antMatchers(HttpMethod.PUT,"/user/*").hasAnyAuthority("ADMIN","MANAGER")
                .anyRequest().authenticated()
                ;

        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
