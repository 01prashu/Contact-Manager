package com.example.Contact_Manager.security;

import com.example.Contact_Manager.service.CustomUserDetails;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfuguration {
    @Autowired
    OauthAuthenticationSucessHandler oauthAuthenticationSucessHandler;
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private CustomAuth2UserService customAuth2UserService;

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return  new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider AuthenticationProvider = new DaoAuthenticationProvider();
        AuthenticationProvider.setPasswordEncoder(passwordEncoder());
        AuthenticationProvider.setUserDetailsService(userDetailsService);
        return AuthenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(token->token.disable()).authorizeHttpRequests(auth->auth.requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/register/**","/users/**","/authenticate","/css/**","/js/**","/images/**","/register-handle","/user/**" ,"/login/**","/webjars/**" , "/oauth2/**", "/public/**").permitAll()
                .requestMatchers("/home/**").authenticated()
                .anyRequest().permitAll()).formLogin(login->login.loginPage("/login").loginProcessingUrl("/authenticate").defaultSuccessUrl("/home",true).permitAll()).logout().permitAll();
        httpSecurity.oauth2Login(auth->{
            auth.loginPage("/login").successHandler(oauthAuthenticationSucessHandler).defaultSuccessUrl("/home",true).userInfoEndpoint().userService(customAuth2UserService);
        });
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        return httpSecurity.build();
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }



}
