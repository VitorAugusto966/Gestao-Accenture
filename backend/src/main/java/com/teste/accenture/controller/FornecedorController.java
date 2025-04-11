/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.teste.accenture.controller;

import com.teste.accenture.dto.FornecedorDTO;
import com.teste.accenture.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *
 * @author vitor
 */

@RestController
@RequestMapping("/api/fornecedor")
@CrossOrigin(origins = "*")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public FornecedorDTO criar(@Valid @RequestBody FornecedorDTO dto) {
        return fornecedorService.toDTO(fornecedorService.salvar(dto));
    }

    @GetMapping
    public List<FornecedorDTO> listar(@RequestParam(required = false) String filtro) {
        return fornecedorService.listar(filtro);
    }

    @GetMapping("/{id}")
    public FornecedorDTO buscarPorId(@PathVariable Long id) {
        return fornecedorService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
    }

    @PutMapping("/{id}")
    public FornecedorDTO atualizar(@PathVariable Long id, @Valid @RequestBody FornecedorDTO dto) {
        return fornecedorService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        fornecedorService.deletar(id);
    }
}
