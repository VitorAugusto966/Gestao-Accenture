<template>
  <div>
    <Header />
    <div class="vinc-container">
      <div class="vinc-formulario">
        <h1>Vincular Fornecedores à Empresa</h1>
        <form @submit.prevent="handleVinculo">
          <div class="vinc-group">
            <label for="nome">Nome Fantasia</label>
            <input id="nome" v-model="empresa.nomeFantasia" disabled />
          </div>

          <div class="vinc-group">
            <label for="cnpj">CNPJ</label>
            <input id="cnpj" v-model="empresa.cnpj" disabled />
          </div>

          <div class="vinc-group">
            <label for="cep">CEP</label>
            <input id="cep" v-model="empresa.cep" disabled />
          </div>

          <button id="vinc-btn" type="submit">Salvar Vínculo</button>
        </form>
      </div>

      <div class="vinc-fornecedores">
        <h2>Selecionar Fornecedores</h2>
        <div class="vinc-tabela-container">
          <table class="vinc-tabela-fornecedores">
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
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useToast } from 'vue-toastification'
import '@/styles/vincEmpresa.css'

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

onMounted(async () => {
  const empresaStr = localStorage.getItem('empresaVinc')
  if (empresaStr) {
    const empresaData = JSON.parse(empresaStr)
    empresa.value = {
      id: empresaData.id,
      nomeFantasia: empresaData.nomeFantasia,
      cnpj: empresaData.cnpj,
      cep: empresaData.cep,
    }
    fornecedoresSelecionados.value = empresaData.fornecedorIds || []
    localStorage.removeItem('empresaVinc')
  }

  try {
    const res = await axios.get('http://localhost:8080/api/fornecedor')
    fornecedores.value = res.data
  } catch (error) {
    toast.error('Erro ao carregar fornecedores.')
    console.error(error)
  }
})

async function handleVinculo() {
  const payload = {
    nomeFantasia: empresa.value.nomeFantasia,
    cnpj: empresa.value.cnpj.replace(/\D/g, ''),
    cep: empresa.value.cep.replace(/\D/g, ''),
    fornecedorIds: fornecedoresSelecionados.value,
  }

  try {
    await axios.put(`http://localhost:8080/api/empresa/${empresa.value.id}`, payload)
    toast.success('Fornecedores vinculados com sucesso!')
    router.push('/empresas')
  } catch (error) {
    toast.error('Erro ao salvar vínculo.')
    console.error(error)
  }
}
</script>
