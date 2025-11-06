package br.com.meuestudodespring.minha_api;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 1. Diz ao JPA que esta classe é uma tabela no banco
@Entity
// 2. (Opcional) Diz o nome da tabela. Se omitido, seria "tarefa".
@Table(name = "tb_tarefas")
public class Tarefa {

    // 3. Define que este campo é a Chave Primária (PK)
    @Id
    // 4. Define que o valor será gerado pelo banco (auto-incremento)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private boolean concluida;

    // --- Getters e Setters (O JPA precisa deles) ---
    // Você pode gerar na sua IDE (Alt+Shift+S no Eclipse)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public boolean isConcluida() {
        return concluida;
    }
    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}