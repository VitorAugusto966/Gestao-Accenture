package com.teste.accenture.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.teste.accenture.repository.EmpresaRepository;
import com.teste.accenture.repository.FornecedorRepository;
import com.teste.accenture.dto.EmpresaDTO;
import com.teste.accenture.model.Empresa;
import com.teste.accenture.model.Fornecedor;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CepService cepService;

    public EmpresaService(EmpresaRepository empresaRepository, FornecedorRepository fornecedorRepository,
            CepService cepService) {
        this.empresaRepository = empresaRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.cepService = cepService;
    }

    @Transactional
    public Empresa salvar(EmpresaDTO dto) {
        validarDados(dto);

        Set<Fornecedor> fornecedores = carregarFornecedores(dto.getFornecedorIds());

        Empresa empresa = new Empresa();
        empresa.setCnpj(dto.getCnpj());
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCep(dto.getCep());
        empresa.setFornecedores(fornecedores);

        return empresaRepository.save(empresa);
    }

    @Transactional
    public List<EmpresaDTO> listar() {
        //System.out.println("\n entrou aqui \n");
        List<Empresa> empresas = empresaRepository.findAll();
        /*for (Empresa e : empresas) {
            System.out.println("Empresa: " + e.getNomeFantasia());
            System.out.println("Fornecedores: " + e.getFornecedores().stream()
                    .map(Fornecedor::getId)
                    .toList());
        }*/

        return empresas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmpresaDTO> buscarPorId(Long id) {
        return empresaRepository.findById(id).map(this::toDTO);
    }

    @Transactional
    public EmpresaDTO atualizar(Long id, EmpresaDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        validarDados(dto);

        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCep(dto.getCep());

        if (dto.getFornecedorIds() != null) {
            Set<Fornecedor> novosFornecedores = carregarFornecedores(dto.getFornecedorIds());
            empresa.setFornecedores(new HashSet<>(novosFornecedores));
        }

        return toDTO(empresaRepository.save(empresa));
    }

    @Transactional
    public void deletar(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new IllegalArgumentException("Empresa não encontrada");
        }
        empresaRepository.deleteById(id);
    }

    public EmpresaDTO toDTO(Empresa empresa) {

        return EmpresaDTO.builder()
                .id(empresa.getId())
                .cnpj(empresa.getCnpj())
                .nomeFantasia(empresa.getNomeFantasia())
                .cep(empresa.getCep())
                .fornecedorIds(empresa.getFornecedores().stream()
                        .map(Fornecedor::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    private void validarDados(EmpresaDTO dto) {
        if (dto.getCnpj() == null || dto.getCnpj().length() != 14) {
            throw new IllegalArgumentException("CNPJ inválido");
        }

        if (!cepService.isCepValido(dto.getCep())) {
            throw new IllegalArgumentException("CEP inválido");
        }
    }

    private Set<Fornecedor> carregarFornecedores(Set<Long> fornecedorIds) {
        if (fornecedorIds == null || fornecedorIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(fornecedorRepository.findAllById(fornecedorIds));
    }
}
