package br.com.meuestudodespring.minha_api.auth;

// Um 'record' já cria os campos, construtor, getters, etc.
public record RegisterDTO(String login, String password) {
}
