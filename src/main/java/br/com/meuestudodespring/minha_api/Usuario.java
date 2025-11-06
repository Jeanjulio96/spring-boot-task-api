package br.com.meuestudodespring.minha_api;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_usuarios")
public class Usuario implements UserDetails { // IMPORTANTE: Implementa UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true) // O login (email) deve ser único
    private String login; // Vamos usar email como login
    
    private String password;

    // Construtor vazio (JPA precisa)
    public Usuario() {}

    // Construtor com campos
    public Usuario(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // --- Métodos do UserDetails (Spring Security) ---
    // Precisamos implementar isso para o Spring Security entender
    // como nosso "Usuario" funciona.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, não vamos usar "Roles" (ex: ADMIN, USER)
        // Devolvemos uma lista vazia.
        return List.of(); 
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        // Nosso "username" é o campo "login"
        return this.login;
    }

    // Métodos para conta não expirada, não bloqueada, etc.
    // Por enquanto, todos retornam 'true'.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }
}
