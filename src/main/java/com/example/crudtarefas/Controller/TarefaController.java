package com.example.crudtarefas.Controller;

import com.example.crudtarefas.Class.Tarefa;
import com.example.crudtarefas.Service.TarefaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody Tarefa body, BindingResult br) {
        if (br.hasErrors()) {
            Map<String, String> erros = br.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            DefaultMessageSourceResolvable::getDefaultMessage,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));
            return ResponseEntity.badRequest().body(Map.of("erros", erros));
        }
        Tarefa salva = service.criar(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @GetMapping
    public List<Tarefa> listar() {
        return service.listar();
    }

    @GetMapping("/ativas")
    public List<Tarefa> listarAtivas(@RequestParam(required = false) String status) {
        if (status == null) return service.listarAtivas();
        return service.buscarAtivasPorStatus(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscar(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/ativas/{id}")
    public ResponseEntity<?> buscarAtivas(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarAtivos(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        try {
            return ResponseEntity.ok(service.atualizarParcial(id, campos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void Inativar(@PathVariable Long id) {
        service.Inativar(id);
    }
}
