package com.example.crudtarefas.Service;


import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.example.crudtarefas.Class.Tarefa;
import com.example.crudtarefas.Repository.TarefaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeParseException;

@Service
public class TarefaService {

    private final TarefaRepository repo;

    public TarefaService(TarefaRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Tarefa criar(Tarefa t) {
        t.setId(null);
        t.setStatus(Tarefa.Status.ABERTA);
        return repo.save(t);
    }

    public java.util.List<Tarefa> listar() {
        return repo.findAll();
    }

    public java.util.List<Tarefa> listarAtivas() {
        return repo.findByInativoEmIsNull();
    }

    public Tarefa buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Tarefa não encontrada"));
    }

    public Tarefa buscarAtivos(Long id) {
        return repo.findByIdAndInativoEmIsNull(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Tarefa não encontrada"));
    }

    public java.util.List<Tarefa> buscarAtivasPorStatus(String status) {
        try {
            Tarefa.Status s = Tarefa.Status.valueOf(status.toUpperCase());
            return repo.findByStatusAndInativoEmIsNull(s);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("status inválido. Use: ABERTA, EM_ANDAMENTO, CONCLUIDA, CANCELADA");
        }
    }

    @Transactional
    public Tarefa Inativar(Long id) {
        Tarefa atual = buscarAtivos(id);
        atual.setInativoEm(LocalDateTime.now());
        atual.setStatus(Tarefa.Status.CANCELADA);
        return repo.save(atual);
    }

    @Transactional
    public Tarefa atualizarParcial(Long id, java.util.Map<String, Object> campos) {
        Tarefa atual = buscarAtivos(id);

        if (campos.containsKey("titulo")) {
            String v = (String) campos.get("titulo");
            if (v == null || v.isBlank()) throw new IllegalArgumentException("título obrigatório");
            atual.setTitulo(v);
        }

        if (campos.containsKey("descricao")) {
            String v = (String) campos.get("descricao");
            if (v == null || v.isBlank()) throw new IllegalArgumentException("descrição é um campo obrigatório");
            atual.setDescricao(v);
        }

        if (campos.containsKey("status")) {
            String v = ((String) campos.get("status")).toUpperCase();
            try { atual.setStatus(Tarefa.Status.valueOf(v)); }
            catch (IllegalArgumentException e) { throw new IllegalArgumentException("status inválido. Use: ABERTA, EM_ANDAMENTO, CONCLUIDA, CANCELADA"); }
        }

        if (campos.containsKey("prazo")) {
            String v = (String) campos.get("prazo");
            try { atual.setPrazo(java.time.LocalDate.parse(v)); }
            catch (DateTimeParseException e) { throw new IllegalArgumentException("prazo deve estar no formato yyyy-MM-dd"); }
        }

        return repo.save(atual);
    }
}
