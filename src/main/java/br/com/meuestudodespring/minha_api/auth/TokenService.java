package br.com.meuestudodespring.minha_api.auth;

import br.com.meuestudodespring.minha_api.Usuario;
import br.com.meuestudodespring.minha_api.auth.TokenService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // 1. Injeta o segredo do application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    // 2. Método para GERAR um token
    public String gerarToken(Usuario usuario) {
        try {
            // Define o algoritmo de assinatura (HMAC256) com nosso segredo
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            
            String token = JWT.create()
                    .withIssuer("minha-api") // Quem está emitindo o token
                    .withSubject(usuario.getLogin()) // Quem é o "dono" do token
                    .withExpiresAt(gerarDataExpiracao()) // Data de expiração
                    .sign(algoritmo); // Assina o token
            return token;
            
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // 3. Método para VALIDAR um token e pegar o Subject (login)
    public String validarToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            
            return JWT.require(algoritmo)
                    .withIssuer("minha-api")
                    .build()
                    .verify(token) // Verifica a assinatura
                    .getSubject(); // Pega o "dono" (login)
                    
        } catch (JWTVerificationException exception) {
            // Retorna vazio se o token for inválido (expirado, assinatura errada, etc)
            return ""; 
        }
    }

    // 4. Define a data de expiração (ex: 2 horas a partir de agora)
    private Instant gerarDataExpiracao() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00")); // Fuso de Brasília
    }
}