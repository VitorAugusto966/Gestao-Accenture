<template>
  <div>
    <Header />
    <div class="container-lista-f">
      <h1 class="titulo-f">📋 Fornecedores Cadastrados</h1>

      <div class="filtros">
        <input
          v-model="filtroNome"
          type="text"
          placeholder="🔍 Filtrar por Nome"
          class="filtro-input"
        />
        <input
          v-model="filtroDocumento"
          type="text"
          placeholder="🔍 Filtrar por CPF/CNPJ"
          class="filtro-input"
        />
      </div>

      <div class="tabela-wrapper">
        <table class="tabela">
          <thead>
            <tr>
              <th class="tr-title">Nome</th>
              <th class="tr-title">Tipo</th>
              <th class="tr-title">Documento</th>
              <th class="tr-title">CEP</th>
              <th class="acoes">Ações</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="fornecedor in fornecedoresFiltrados" :key="fornecedor.id">
              <td>{{ fornecedor.nome }}</td>
              <td>{{ formatarTipo(fornecedor.tipoPessoa) }}</td>
              <td>{{ mascararDocumento(fornecedor.documento, fornecedor.tipoPessoa) }}</td>
              <td>{{ formatarCep(fornecedor.cep) }}</td>
              <td class="acoes">
                <button class="btn editar" @click="editarFornecedor(fornecedor.id)">Editar</button>
                <button class="btn excluir" @click="excluirFornecedor(fornecedor.id)">
                  Excluir
                </button>
              </td>
            </tr>
            <tr v-if="fornecedoresFiltrados.length === 0">
              <td colspan="5" style="text-align: center; padding: 1rem">
                Nenhum fornecedor encontrado.
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import Header from '@/components/Header.vue'
import '@/styles/listFornecedor.css'
import { mascararDocumento } from '@/utils/mascaraInput'

interface Fornecedor {
  id: number
  nome: string
  tipoPessoa: string
  documento: string
  cep: string
}

const fornecedores = ref<Fornecedor[]>([])
const toast = useToast()
const router = useRouter()

const filtroNome = ref('')
const filtroDocumento = ref('')

onMounted(async () => {
  await carregarFornecedores()
})

async function carregarFornecedores() {
  try {
    const res = await axios.get('http://localhost:8080/api/fornecedor')
    fornecedores.value = res.data
  } catch (error) {
    toast.error('Erro ao carregar fornecedores.')
    console.error(error)
  }
}

function editarFornecedor(id: number) {
  const fornecedor = fornecedores.value.find((e) => e.id === id)
  if (fornecedor) {
    localStorage.setItem('fornecedor', JSON.stringify(fornecedor))
    router.push(`/fornecedor/editar/`)
  }
}

async function excluirFornecedor(id: number) {
  if (confirm('Tem certeza que deseja excluir este fornecedor?')) {
    try {
      await axios.delete(`http://localhost:8080/api/fornecedor/${id}`)
      toast.success('Fornecedor excluído com sucesso.')
      await carregarFornecedores()
    } catch (error) {
      toast.error('Erro ao excluir fornecedor.')
      console.error(error)
    }
  }
}

function formatarTipo(tipo: string) {
  return tipo === 'FISICA' ? 'Pessoa Física' : 'Pessoa Jurídica'
}

function formatarCep(cep: string) {
  const numeros = cep.replace(/\D/g, '')
  if (numeros.length === 8) {
    return numeros.replace(/^(\d{5})(\d{3})$/, '$1-$2')
  }
  return cep
}

const fornecedoresFiltrados = computed(() => {
  const nome = filtroNome.value.trim().toLowerCase()
  const doc = filtroDocumento.value.replace(/\D/g, '')
  return fornecedores.value.filter((f) => {
    const nomeMatch = f.nome.toLowerCase().includes(nome)
    const docMatch = f.documento.replace(/\D/g, '').includes(doc)
    return nomeMatch && docMatch
  })
})
</script>
