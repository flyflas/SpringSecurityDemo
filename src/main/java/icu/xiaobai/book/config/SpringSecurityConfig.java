package icu.xiaobai.book.config;

import icu.xiaobai.book.security.filter.AuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class SpringSecurityConfig {
    AuthenticationEntryPoint authenticationEntryPoint;
    AccessDeniedHandler accessDeniedHandler;
    LogoutSuccessHandler logoutSuccessHandler;
    AuthenticationSuccessHandler authenticationSuccessHandler;
    AuthenticationFailureHandler authenticationFailureHandler;
    AuthenticationTokenFilter authenticationTokenFilter;

    public SpringSecurityConfig(AuthenticationEntryPoint authenticationEntryPoint,
                                AccessDeniedHandler accessDeniedHandler,
                                LogoutSuccessHandler logoutSuccessHandler,
                                AuthenticationSuccessHandler authenticationSuccessHandler,
                                AuthenticationFailureHandler authenticationFailureHandler,
                                AuthenticationTokenFilter authenticationTokenFilter) {

        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationTokenFilter = authenticationTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers("/login").anonymous()
                .requestMatchers("/reset/password/**").anonymous()
                .requestMatchers("/account/signup/**").anonymous()
                .requestMatchers("/account/limitedToken").anonymous()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.formLogin()
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler).permitAll();

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
