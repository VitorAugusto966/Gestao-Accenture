/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.teste.accenture.dto;

import lombok.*;
import java.util.Set;

/**
 *
 * @author vitor
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaDTO {
    private Long id;
    private String cnpj;
    private String nomeFantasia;
    private String cep;
    private Set<Long> fornecedorIds;
}
