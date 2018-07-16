package com.sipios.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin().and()
                .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/static/**/*", "/css/**/*", "/js/**/*", "/images/**/*").permitAll()
                .antMatchers("/user/*/clients", "/user/*/clients/**").hasRole("ADMIN")
                .antMatchers("/restricted/**").hasRole("USER")

                .antMatchers("/h2", "/h2/**").hasRole("SUPER_ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin().loginPage("/se-connecter").failureUrl("/login-error").defaultSuccessUrl("/user-redirect")
                .permitAll()
                .and()
            .logout()
                .permitAll();

    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}
