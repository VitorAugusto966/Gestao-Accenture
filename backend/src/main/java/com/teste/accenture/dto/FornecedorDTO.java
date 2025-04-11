/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.teste.accenture.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.Set;

/**
 *
 * @author vitor
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorDTO {
    private Long id;
    private String documento;
    private String nome;
    private String email;
    private String cep;
    private String tipoPessoa;
    private String rg;
    private LocalDate dataNascimento;
    private Set<Long> empresaIds;
}
