package com.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class MyWebSecurity extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    @Autowired
    private UserAuthenticationSuccessHandler userAuthenticationSuccessHandler;

    public MyWebSecurity(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService1) {

        this.userDetailsService = userDetailsService1;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()
                .antMatchers("/books/*").hasRole("USER")
                .antMatchers("/authors/**", "/books/*").hasRole("ADMIN")
                .antMatchers("/", "/webjars/**", "/register", "/login*", "/signin/**", "/signup/**", "/registerConfirm").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .successHandler(userAuthenticationSuccessHandler)
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/app-login")
                .usernameParameter("username")
                .passwordParameter("password")
                //.defaultSuccessUrl("/userDash", true)
                .failureUrl("/login/error")


                .and()
                .logout().permitAll()
                .logoutUrl("/app-logout")
                .clearAuthentication(true)
                .logoutSuccessUrl("/login")


                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())

                .and()
                .httpBasic();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(
                    HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse,
                    AccessDeniedException e) throws IOException, ServletException {

                httpServletResponse.sendRedirect("/accessDenied");
            }

        };
    }
}
