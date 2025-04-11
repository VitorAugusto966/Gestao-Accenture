<template>
  <div>
    <Header />
    <div class="cadastro-container">
      <div class="formulario">
        <h1>Cadastrar Empresa</h1>
        <form @submit.prevent="handleSubmit">
          <div class="form-group-e">
            <label for="nome">Nome Fantasia</label>
            <input id="nome" v-model="empresa.nomeFantasia" required />
          </div>

          <div class="form-group-e">
            <label for="cnpj">CNPJ</label>
            <input
              id="cnpj"
              v-model="empresa.cnpj"
              @input="mascararCNPJ"
              required
              placeholder="00.000.000/0000-00"
            />
          </div>

          <div class="form-group-e">
            <label for="cep">CEP</label>
            <input
              id="cep"
              v-model="empresa.cep"
              @input="mascararCep"
              required
              placeholder="00000-000"
            />
          </div>

          <button id="cadE" type="submit">Cadastrar</button>
        </form>
      </div>

      <div class="fornecedores">
        <h2>Fornecedores</h2>
        <div class="tabela-container">
          <table class="tabela-fornecedores">
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
import { useToast } from 'vue-toastification'
import { validarCNPJ } from '@/utils/validador'
import '@/styles/cadEmpresa.css'

const toast = useToast()

const empresa = ref({
  nomeFantasia: '',
  cnpj: '',
  cep: '',
})

const fornecedores = ref<any[]>([])
const fornecedoresSelecionados = ref<number[]>([])

onMounted(async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/fornecedor')
    fornecedores.value = res.data
  } catch (error) {
    toast.error('Erro ao carregar fornecedores.')
    console.error(error)
  }
})

function limparMascara(str: string) {
  return str.replace(/\D/g, '')
}

function mascararCNPJ() {
  let valor = empresa.value.cnpj.replace(/\D/g, '')
  if (valor.length > 14) valor = valor.slice(0, 14)
  valor = valor
    .replace(/^(\d{2})(\d)/, '$1.$2')
    .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/\.(\d{3})(\d)/, '.$1/$2')
    .replace(/(\d{4})(\d)/, '$1-$2')
  empresa.value.cnpj = valor
}

function mascararCep() {
  let valor = empresa.value.cep.replace(/\D/g, '')
  if (valor.length > 8) valor = valor.slice(0, 8)
  if (valor.length > 5) {
    valor = valor.slice(0, 5) + '-' + valor.slice(5)
  }
  empresa.value.cep = valor
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

async function handleSubmit() {
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
    await axios.post('http://localhost:8080/api/empresa', payload)
    toast.success('Empresa cadastrada com sucesso!')
    empresa.value = { nomeFantasia: '', cnpj: '', cep: '' }
    fornecedoresSelecionados.value = []
  } catch (error) {
    toast.error('Erro ao cadastrar empresa')
    console.error(error)
  }
}
</script>
