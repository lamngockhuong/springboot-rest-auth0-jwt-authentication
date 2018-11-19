package com.ngockhuong.example.config;

import com.ngockhuong.example.filter.JWTAuthenticationFilter;
import com.ngockhuong.example.filter.JWTLoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .antMatchers(HttpMethod.GET, "/login").permitAll() // For test on browser
            // Need authentication
            .anyRequest().authenticated()
            .and()
            //
            // Add Filter 1 - JWTLoginFilter
            //
            .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
            //
            // Add Filter 2 - JWTAuthenticationFilter
            //
            .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String password = "123456";
        String encryptedPassword = passwordEncoder().encode(password);
        log.info("Encoded password of 123456=" + encryptedPassword);

        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer = auth.inMemoryAuthentication();
        UserDetails user1 = User.withUsername("ngoc").password(encryptedPassword).roles("USER").build();
        UserDetails user2 = User.withUsername("khuong").password(encryptedPassword).roles("USER").build();
        configurer.withUser(user1);
        configurer.withUser(user2);
    }
}
