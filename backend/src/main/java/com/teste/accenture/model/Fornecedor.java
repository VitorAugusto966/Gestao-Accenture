package com.teste.accenture.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documento;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String tipoPessoa;

    private String rg;

    private LocalDate dataNascimento;

    @ManyToMany(mappedBy = "fornecedores")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Empresa> empresas = new HashSet<>();
}
