package br.com.meuestudodespring.minha_api.auth; // 1. Pacote CORRETO

// 2. Mude de 'public class LoginResponseDTO { }' 
//    para 'public record LoginResponseDTO(String token) { }'
//
// Esta linha SOZINHA cria:
// - um campo 'private final String token'
// - um construtor 'public LoginResponseDTO(String token)'
// - um getter 'public String token()'
public record LoginResponseDTO(String token) {
}