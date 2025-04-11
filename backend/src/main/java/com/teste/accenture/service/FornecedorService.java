package com.teste.accenture.service;

import com.teste.accenture.dto.FornecedorDTO;
import com.teste.accenture.model.Empresa;
import com.teste.accenture.model.Fornecedor;
import com.teste.accenture.repository.EmpresaRepository;
import com.teste.accenture.repository.FornecedorRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final EmpresaRepository empresaRepository;
    private final CepService cepService;

    public FornecedorService(FornecedorRepository fornecedorRepository, EmpresaRepository empresaRepository,
            CepService cepService) {
        this.fornecedorRepository = fornecedorRepository;
        this.empresaRepository = empresaRepository;
        this.cepService = cepService;
    }

    @Transactional
    public Fornecedor salvar(FornecedorDTO dto) {
        validarDados(dto);

        Set<Empresa> empresas = dto.getEmpresaIds() != null
                ? new HashSet<>(empresaRepository.findAllById(dto.getEmpresaIds()))
                : new HashSet<>();

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setDocumento(dto.getDocumento());
        fornecedor.setNome(dto.getNome());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setCep(dto.getCep());
        fornecedor.setTipoPessoa(dto.getTipoPessoa());
        fornecedor.setRg(dto.getRg());
        fornecedor.setDataNascimento(dto.getDataNascimento());
        fornecedor.setEmpresas(empresas);

        return fornecedorRepository.save(fornecedor);
    }

    public List<FornecedorDTO> listar(String nomeOuDocumento) {
        List<Fornecedor> fornecedores;

        if (nomeOuDocumento != null && !nomeOuDocumento.isBlank()) {
            String termo = nomeOuDocumento.toLowerCase();
            fornecedores = fornecedorRepository.findAll().stream()
                    .filter(f -> f.getNome().toLowerCase().contains(termo)
                            || f.getDocumento().contains(nomeOuDocumento))
                    .collect(Collectors.toList());
        } else {
            fornecedores = fornecedorRepository.findAll();
        }

        return fornecedores.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<FornecedorDTO> buscarPorId(Long id) {
        return fornecedorRepository.findById(id).map(this::toDTO);
    }

    @Transactional
    public FornecedorDTO atualizar(Long id, FornecedorDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado"));

        if (!cepService.isCepValido(dto.getCep())) {
            throw new IllegalArgumentException("CEP inválido");
        }

        fornecedor.setNome(dto.getNome());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setCep(dto.getCep());

        return toDTO(fornecedorRepository.save(fornecedor));
    }

    @Transactional
    public void deletar(Long id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new IllegalArgumentException("Fornecedor não encontrado");
        }
        fornecedorRepository.deleteById(id);
    }

    public FornecedorDTO toDTO(Fornecedor fornecedor) {
        return FornecedorDTO.builder()
                .id(fornecedor.getId())
                .documento(fornecedor.getDocumento())
                .nome(fornecedor.getNome())
                .email(fornecedor.getEmail())
                .cep(fornecedor.getCep())
                .rg(fornecedor.getRg())
                .dataNascimento(fornecedor.getDataNascimento())
                .tipoPessoa(fornecedor.getTipoPessoa())
                .empresaIds(fornecedor.getEmpresas().stream().map(Empresa::getId).collect(Collectors.toSet()))
                .build();
    }

    private void validarDados(FornecedorDTO dto) {
        if (!cepService.isCepValido(dto.getCep())) {
            throw new IllegalArgumentException("CEP inválido");
        }

        if (fornecedorRepository.findByDocumento(dto.getDocumento()).isPresent()) {
            throw new IllegalArgumentException("Documento já cadastrado");
        }

        if ("PF".equalsIgnoreCase(dto.getTipoPessoa())) {
            if (dto.getRg() == null || dto.getDataNascimento() == null) {
                throw new IllegalArgumentException("RG e Data de Nascimento são obrigatórios para pessoa física");
            }

            for (Long empresaId : Optional.ofNullable(dto.getEmpresaIds()).orElse(Set.of())) {
                Empresa empresa = empresaRepository.findById(empresaId)
                        .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada: " + empresaId));

                if (cepService.isEmpresaDoParana(empresa.getCep())) {
                    int idade = Period.between(dto.getDataNascimento(), LocalDate.now()).getYears();
                    if (idade < 18) {
                        throw new IllegalArgumentException(
                                "Pessoa física menor de idade não pode ser cadastrada em empresa do PR");
                    }
                }
            }
        }
    }
}
