package com.example.crudtarefas.Class;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="título obrigatório")
    @Column(nullable = false, length = 120)
    private String titulo;

    @NotBlank(message="descrição é um campo obrigatório")
    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    public enum Status { ABERTA, EM_ANDAMENTO, CONCLUIDA, CANCELADA }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.ABERTA;

    @NotNull (message="prazo é um campo obrigatório")
    @JsonFormat (pattern = "yyyy-MM-dd") // opcional
    @Column(nullable = false)
    private LocalDate prazo;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    private LocalDateTime inativoEm;

    public Tarefa() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public void setInativoEm(LocalDateTime inativoEm) {
        this.inativoEm = inativoEm;
    }

    public LocalDateTime getInativoEm() {
        return inativoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public Long getId() {
        return id;
    }
}