package com.example.graphql.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig {

    @Bean
    fun springWebFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { c -> c.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/graphql", "/graphiql").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic(withDefaults())
            .build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val userBuilder = User.withDefaultPasswordEncoder()
        val user: UserDetails = userBuilder.username("user").password("user").roles("USER").build()
        val admin: UserDetails = userBuilder.username("admin").password("admin").roles("USER", "ADMIN").build()
        return InMemoryUserDetailsManager(user, admin)
    }
}