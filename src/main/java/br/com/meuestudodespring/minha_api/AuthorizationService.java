package br.com.meuestudodespring.minha_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 1. Diz ao Spring que esta é uma classe de serviço
@Service
public class AuthorizationService implements UserDetailsService {

    // 2. Injeta nosso repositório de usuários
    @Autowired
    private UsuarioRepository usuarioRepository;

    // 3. Este é o método que o Spring Security chama
    //    quando tentamos fazer login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Nós dissemos que nosso "username" é o "login"
        UserDetails user = usuarioRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return user;
    }
}