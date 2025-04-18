package cl.fes.apiUsuarios.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // Desactiva la protección CSRF (para APIs)
        	.authorizeHttpRequests(auth -> auth
        		.requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
        		.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
        		.requestMatchers(
        	            new AntPathRequestMatcher("/swagger-ui/**"),
        	            new AntPathRequestMatcher("/swagger-ui.html"),
        	            new AntPathRequestMatcher("/v3/api-docs/**"),
        	            new AntPathRequestMatcher("/swagger-resources/**"),
        	            new AntPathRequestMatcher("/webjars/**")
        	        ).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
            .headers().frameOptions().sameOrigin();  // Permitir mostrar el iframe para H2 Console

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); 
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}