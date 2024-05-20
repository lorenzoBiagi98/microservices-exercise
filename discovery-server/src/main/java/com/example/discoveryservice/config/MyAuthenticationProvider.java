package com.example.discoveryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableWebSecurity
public class MyAuthenticationProvider implements AuthenticationProvider {

    /*
    In questo modo vengono iniettati i valori presenti in application.properties, nonostante questa
    configurazione sia hardcodata, e questi vengano comunque immessi forzatamente nel metodo
    di autenticazione
     */
    @Value("${eureka.username}")
    private String username;
    @Value("${eureka.password}")
    private String password;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if("eureka".equals(username)&&"password".equals(password)){
        UserDetails userDetails = User.withUsername(username)
                .password(password)
                .roles("USER")
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    } else {
        throw new BadCredentialsException("Invalid username or password");
    }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
