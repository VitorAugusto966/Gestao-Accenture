/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.teste.accenture.controller;

import com.teste.accenture.dto.EmpresaDTO;
import com.teste.accenture.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *
 * @author vitor
 */
@RestController
@RequestMapping("/api/empresa")
@CrossOrigin(origins = "*")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public EmpresaDTO criar(@Valid @RequestBody EmpresaDTO dto) {
        return empresaService.toDTO(empresaService.salvar(dto));
    }

    @GetMapping
    public List<EmpresaDTO> listar() {
        return empresaService.listar();
    }

    @GetMapping("/{id}")
    public EmpresaDTO buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    @PutMapping("/{id}")
    public EmpresaDTO atualizar(@PathVariable Long id, @Valid @RequestBody EmpresaDTO dto) {
        return empresaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        empresaService.deletar(id);
    }
}
