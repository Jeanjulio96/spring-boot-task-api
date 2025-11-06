package br.com.meuestudodespring.minha_api.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.meuestudodespring.minha_api.UsuarioRepository;

import java.io.IOException;

// 1. Diz ao Spring que este é um componente (um "filtro")
@Component 
// 2. Garante que o filtro rode apenas UMA VEZ por requisição
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Este é o método que intercepta a requisição
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 3. Tenta recuperar o token do cabeçalho
        var token = this.recuperarToken(request);

        // 4. Se existir um token...
        if (token != null) {
            // 5. Valida o token e pega o login (subject) de dentro dele
            var login = tokenService.validarToken(token);
            
            // 6. Busca o usuário no banco de dados com base no login
            UserDetails usuario = usuarioRepository.findByLogin(login);

            // 7. Se o usuário existir, nós o "autenticamos" para esta requisição
            if (usuario != null) {
                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario, null, usuario.getAuthorities());
                
                // 8. Salva a autenticação no Contexto de Segurança do Spring
                //    Agora o Spring sabe que este usuário está logado!
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // 9. (Importante!) Continua a cadeia de filtros.
        //    Se o token não existiu ou foi inválido, o Contexto continua vazio,
        //    e o próximo filtro (do Spring) vai barrar a requisição.
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para pegar o token do Header "Authorization"
    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        // O token vem como "Bearer <token>". Queremos apenas o <token>.
        return authHeader.replace("Bearer ", "");
    }
}
