<template>
  <div>
    <Header />
    <div class="container-lista">
      <h1 class="titulo">📋 Empresas Cadastradas</h1>
      <div class="tabela-wrapper">
        <table class="tabela">
          <thead>
            <tr style="background-color: #007bff">
              <th class="tr-title">Nome Fantasia</th>
              <th class="tr-title">CNPJ</th>
              <th class="tr-title">CEP</th>
              <th class="acoes">Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="empresa in empresas" :key="empresa.id">
              <td>{{ empresa.nomeFantasia }}</td>
              <td>{{ formatarCNPJ(empresa.cnpj) }}</td>
              <td>{{ formatarCEP(empresa.cep) }}</td>
              <td class="acoes">
                <button class="btn editar" @click="editarEmpresa(empresa.id)">Editar</button>
                <button class="btn excluir" @click="excluirEmpresa(empresa.id)">Excluir</button>
                <button class="btn vincular" @click="vincularFornecedor(empresa.id)">
                  Vincular
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import Header from '@/components/Header.vue'
import '@/styles/listEmpresa.css'

interface Empresa {
  id: number
  nomeFantasia: string
  cnpj: string
  cep: string
}

const empresas = ref<Empresa[]>([])
const toast = useToast()
const router = useRouter()

onMounted(async () => {
  await carregarEmpresas()
})

async function carregarEmpresas() {
  try {
    const res = await axios.get('http://localhost:8080/api/empresa')
    empresas.value = res.data
  } catch (error) {
    toast.error('Erro ao carregar empresas.')
    console.error(error)
  }
}

function editarEmpresa(id: number) {
  const empresa = empresas.value.find((e) => e.id === id)
  if (empresa) {
    localStorage.setItem('empresaEditando', JSON.stringify(empresa))
    router.push(`/empresa/editar/`)
  }
}

function vincularFornecedor(id: number) {
  const empresa = empresas.value.find((e) => e.id === id)
  if (empresa) {
    localStorage.setItem('empresaVinc', JSON.stringify(empresa))
    router.push(`/empresa/vincular/`)
  }
}

async function excluirEmpresa(id: number) {
  if (confirm('Tem certeza que deseja excluir esta empresa?')) {
    try {
      await axios.delete(`http://localhost:8080/api/empresa/${id}`)
      toast.success('Empresa excluída com sucesso.')
      await carregarEmpresas()
    } catch (error) {
      toast.error('Erro ao excluir empresa.')
      console.error(error)
    }
  }
}

function formatarCNPJ(cnpj: string): string {
  const cnpjLimpo = cnpj.replace(/\D/g, '')
  if (cnpjLimpo.length !== 14) return cnpj
  return cnpjLimpo.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, '$1.$2.$3/$4-$5')
}

function formatarCEP(cep: string): string {
  const cepLimpo = cep.replace(/\D/g, '')
  if (cepLimpo.length !== 8) return cep
  return cepLimpo.replace(/^(\d{5})(\d{3})$/, '$1-$2')
}
</script>
