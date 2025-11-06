package br.com.meuestudodespring.minha_api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 1. Diz ao Spring que esta é uma interface de repositório
@Repository
// 2. Estendemos JpaRepository, passando <QualClasse, TipoDaChavePrimaria>
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    
    // É SÓ ISSO!
    
    // O Spring Data JPA vai ler o nome deste método e criar a query SQL:
    // "SELECT * FROM tb_tarefas WHERE titulo = ?"
    // List<Tarefa> findByTitulo(String titulo);
}