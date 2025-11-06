package br.com.meuestudodespring.minha_api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // --- ADICIONE ESTA INJEÇÃO ---
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) 
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                    
                    // --- MUDANÇA IMPORTANTE ---
                    // Agora, o endpoint de Tarefas exige autenticação
                    .requestMatchers(HttpMethod.POST, "/api/tarefas").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/tarefas").authenticated()
                    // (Se tivéssemos PUT e DELETE, também iriam aqui)
                    
                    .anyRequest().authenticated() 
                )
                
                // --- ADICIONE ESTA LINHA ---
                // Diz ao Spring para usar nosso filtro (securityFilter)
                // ANTES do filtro padrão de login (UsernamePasswordAuthenticationFilter)
               .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                
                .build();
    }

    // 7. Expõe o AuthenticationManager (gerenciador de autenticação)
    // para ser usado em outros pontos da aplicação (ex: no controller de login)
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 8. Define o "codificador de senhas"
    // Vamos usar o BCrypt. NUNCA salve senhas em texto puro!
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}