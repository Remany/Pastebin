package ru.romanov.pastbin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.romanov.pastbin.services.PersonDetailsService;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
public class SecurityConfig {
    private final PersonDetailsService personDetailsService;
    private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter) {
        this.personDetailsService = personDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers(OPTIONS)
                                .permitAll()
                                .requestMatchers("/pastebin/auth/login", "/pastebin/auth/registration", "/error")
                                .permitAll()
                                .requestMatchers("/pastebin/test") // TODO убрать
                                .permitAll()
                                .anyRequest()
                                .hasAnyRole("USER", "ADMIN"))
                .sessionManagement((session) ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin((login) ->
                        login
                                .loginPage("/pastebin/auth/login")
                                .permitAll())
                .logout(LogoutConfigurer::permitAll)
                .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
