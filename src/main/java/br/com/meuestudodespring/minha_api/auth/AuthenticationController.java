package br.com.meuestudodespring.minha_api.auth;

// Imports do Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Imports do RabbitMQ
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import br.com.meuestudodespring.minha_api.config.RabbitMQConfig;

// Imports do nosso projeto
import br.com.meuestudodespring.minha_api.Usuario;
import br.com.meuestudodespring.minha_api.UsuarioRepository;


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

    // Injeção do RabbitTemplate para enviar mensagens
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // --- Endpoint de LOGIN ---
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var usuario = (Usuario) auth.getPrincipal();

        var token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    // --- Endpoint de REGISTRO (com RabbitMQ) ---
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        // 1. Verifica se o usuário já existe
        if (this.usuarioRepository.findByLogin(data.login()) != null) {
            return ResponseEntity.badRequest().body("Usuário já existe");
        }

        // 2. Criptografa a senha
        String senhaCriptografada = passwordEncoder.encode(data.password());

        // 3. Cria e salva o novo usuário no banco
        Usuario novoUsuario = new Usuario(data.login(), senhaCriptografada);
        this.usuarioRepository.save(novoUsuario);

        // 4. [RABBITMQ] Envia a mensagem para a fila
        //    (A resposta ao usuário não espera isso terminar)
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME, 
            RabbitMQConfig.ROUTING_KEY, 
            novoUsuario.getLogin() // O corpo da mensagem é o e-mail (login)
        );
        
        // 5. Retorna 201 Created IMEDIATAMENTE
        return ResponseEntity.status(201).build();
    }
}