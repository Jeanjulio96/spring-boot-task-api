package br.com.meuestudodespring.minha_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // O Spring Data JPA vai criar a query: "SELECT * FROM tb_usuarios WHERE login = ?"
    // O Spring Security vai usar isso para buscar o usuário pelo username.
    UserDetails findByLogin(String login);
}