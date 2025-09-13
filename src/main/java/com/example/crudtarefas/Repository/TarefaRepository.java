package com.example.crudtarefas.Repository;
import com.example.crudtarefas.Class.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    @Override
    List<Tarefa> findAll();

    @Override
    Optional<Tarefa> findById(Long aLong);

    List<Tarefa> findByInativoEmIsNull();

    List<Tarefa> findByStatusAndInativoEmIsNull(Tarefa.Status status);

    Optional<Tarefa> findByIdAndInativoEmIsNull(Long id);

}