<template>
  <div>
    <Header />
    <div class="edit-container">
      <div class="edit-formulario">
        <h1>Editar Empresa</h1>
        <form @submit.prevent="handleUpdate">
          <div class="edit-group">
            <label for="nome">Nome Fantasia</label>
            <input id="nome" v-model="empresa.nomeFantasia" required />
          </div>

          <div class="edit-group">
            <label for="cnpj">CNPJ</label>
            <input id="cnpj" v-model="empresa.cnpj" disabled placeholder="00.000.000/0000-00" />
          </div>

          <div class="edit-group">
            <label for="cep">CEP</label>
            <input id="cep" v-model="empresa.cep" required placeholder="00000-000" />
          </div>

          <button id="edit-btn" type="submit">Salvar Alterações</button>
        </form>
      </div>

      <div class="edit-fornecedores">
        <h2>Fornecedores</h2>
        <div class="edit-tabela-container">
          <table class="edit-tabela-fornecedores">
            <thead>
              <tr>
                <th></th>
                <th>Nome</th>
                <th>Documento</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="fornecedor in fornecedores" :key="fornecedor.id">
                <td>
                  <input
                    type="checkbox"
                    :value="fornecedor.id"
                    v-model="fornecedoresSelecionados"
                  />
                </td>
                <td>{{ fornecedor.nome }}</td>
                <td>{{ fornecedor.documento }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Header from '@/components/Header.vue'
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import { validarCNPJ } from '@/utils/validador'
import '@/styles/editEmpresa.css'

const toast = useToast()
const router = useRouter()

const empresa = ref({
  id: 0,
  nomeFantasia: '',
  cnpj: '',
  cep: '',
})

const fornecedores = ref<any[]>([])
const fornecedoresSelecionados = ref<number[]>([])

watch(
  () => empresa.value.cnpj,
  (novo) => {
    let valor = novo.replace(/\D/g, '')
    if (valor.length > 14) valor = valor.slice(0, 14)
    valor = valor
      .replace(/^(\d{2})(\d)/, '$1.$2')
      .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
      .replace(/\.(\d{3})(\d)/, '.$1/$2')
      .replace(/(\d{4})(\d)/, '$1-$2')
    empresa.value.cnpj = valor
  },
)

watch(
  () => empresa.value.cep,
  (novo) => {
    let valor = novo.replace(/\D/g, '')
    if (valor.length > 8) valor = valor.slice(0, 8)
    if (valor.length > 5) {
      valor = valor.slice(0, 5) + '-' + valor.slice(5)
    }
    empresa.value.cep = valor
  },
)

onMounted(async () => {
  const empresaStr = localStorage.getItem('empresaEditando')
  if (empresaStr) {
    const empresaEditada = JSON.parse(empresaStr)
    empresa.value = {
      id: empresaEditada.id,
      nomeFantasia: empresaEditada.nomeFantasia,
      cnpj: empresaEditada.cnpj,
      cep: empresaEditada.cep,
    }
    fornecedoresSelecionados.value = empresaEditada.fornecedorIds || []
    localStorage.removeItem('empresaEditando')
  }

  try {
    const res = await axios.get('http://localhost:8080/api/fornecedor')
    fornecedores.value = res.data
  } catch (error) {
    toast.error('Erro ao carregar dados.')
    console.error(error)
  }
})

function limparMascara(str: string) {
  return str.replace(/\D/g, '')
}

async function validarCep(cep: string): Promise<boolean> {
  try {
    const response = await axios.get(`https://viacep.com.br/ws/${cep}/json/`)
    return !response.data.erro
  } catch (error) {
    console.error('Erro ao validar CEP:', error)
    return false
  }
}

async function handleUpdate() {
  const cnpjLimpo = limparMascara(empresa.value.cnpj)
  const cepLimpo = limparMascara(empresa.value.cep)

  if (!validarCNPJ(cnpjLimpo)) {
    toast.error('CNPJ inválido.')
    return
  }

  if (!(await validarCep(cepLimpo))) {
    toast.error('CEP inválido.')
    return
  }

  const payload = {
    nomeFantasia: empresa.value.nomeFantasia,
    cnpj: cnpjLimpo,
    cep: cepLimpo,
    fornecedorIds: fornecedoresSelecionados.value,
  }

  console.log(payload)
  try {
    await axios.put(`http://localhost:8080/api/empresa/${empresa.value.id}`, payload)
    toast.success('Empresa atualizada com sucesso!')
    router.push('/empresa/listar')
  } catch (error) {
    toast.error('Erro ao atualizar empresa.')
    console.error(error)
  }
}
</script>
