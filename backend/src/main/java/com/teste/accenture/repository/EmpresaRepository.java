/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.teste.accenture.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import com.teste.accenture.model.Empresa;

/**
 *
 * @author vitor
 */

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @SuppressWarnings("null")
    @EntityGraph(attributePaths = "fornecedores")
    List<Empresa> findAll();

    Optional<Empresa> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);
}
