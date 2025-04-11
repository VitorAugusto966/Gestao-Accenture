/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.teste.accenture.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.teste.accenture.model.Fornecedor;

/**
 *
 * @author vitor
 */
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    @SuppressWarnings("null")
    @EntityGraph(attributePaths = "empresas")
    List<Fornecedor> findAll();

    Optional<Fornecedor> findByDocumento(String documento);

    boolean existsByDocumento(String documento);
}
