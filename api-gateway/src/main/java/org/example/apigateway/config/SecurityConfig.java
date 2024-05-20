package org.example.apigateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
    serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange->exchange.pathMatchers("/eureka/**")
                    .permitAll()
                    .anyExchange() //Richiedo autorizzazione per tutte le altre richieste
                    .authenticated())
            .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
    return serverHttpSecurity.build();
    }

    /*
    La protezione CRSF viene disattivata nelle app API RESTful
    in quanto non sono vulnerabili a questi attacchi.
     */
}
