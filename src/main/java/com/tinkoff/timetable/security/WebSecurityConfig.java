package com.tinkoff.timetable.security;


import com.tinkoff.timetable.security.user.CustomUserDetailsService;
import com.tinkoff.timetable.security.user.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers(HttpMethod.GET, "/course/**", "lesson/**", "teacher/**")
                    .hasAuthority(Permission.READ.getPermission())
                .antMatchers(HttpMethod.POST, "/course/**", "lesson/**")
                    .hasAuthority(Permission.CREATE.getPermission())
                .antMatchers(HttpMethod.POST, "teacher/**")
                    .hasAuthority(Permission.TEACHER_CONTROL.getPermission())
                .antMatchers(HttpMethod.DELETE, "/course/**", "lesson/**", "teacher/**")
                    .hasAuthority(Permission.DELETE.getPermission())
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/h2-console/**")
                .antMatchers(AUTH_WHITELIST);
    }

}

