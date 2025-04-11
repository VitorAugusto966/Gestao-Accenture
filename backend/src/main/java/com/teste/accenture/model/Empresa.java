package com.teste.accenture.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String nomeFantasia;

    @Column(nullable = false)
    private String cep;

    @ManyToMany
    @JoinTable(name = "empresa_fornecedor", joinColumns = @JoinColumn(name = "empresa_id"), inverseJoinColumns = @JoinColumn(name = "fornecedor_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Fornecedor> fornecedores = new HashSet<>();
}
