package br.com.meuestudodespring.minha_api.auth;

import br.com.meuestudodespring.minha_api.Usuario;
import br.com.meuestudodespring.minha_api.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Endpoint de LOGIN ---
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        // 1. Cria um token de autenticação com login e senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        // 2. O Spring Security (usando o AuthorizationService que criamos)
        //    valida o usuário e senha. Se estiver errado, ele joga uma exceção.
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se o login deu certo, pega o objeto 'Usuario'
        var usuario = (Usuario) auth.getPrincipal();

        // 4. Gera o Token JWT para este usuário
        var token = tokenService.gerarToken(usuario);

        // 5. Retorna o token em um DTO
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    // --- Endpoint de REGISTRO ---
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        // 1. Verifica se o usuário (login) já existe no banco
        if (this.usuarioRepository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().body("Usuário já existe");
        }

        // 2. Se não existe, criptografa a senha (NUNCA SALVE SENHA EM TEXTO PURO)
        String senhaCriptografada = passwordEncoder.encode(data.password());

        // 3. Cria o novo objeto Usuario
        Usuario novoUsuario = new Usuario(data.login(), senhaCriptografada);

        // 4. Salva o usuário no banco
        this.usuarioRepository.save(novoUsuario);

        // 5. Retorna uma resposta de "Criado" (sem corpo)
        return ResponseEntity.status(201).build();
    }
}
