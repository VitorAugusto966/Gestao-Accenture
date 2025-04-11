<template>
  <div>
    <Header />
    <div class="container-f">
      <h1>Cadastrar Fornecedor</h1>
      <form @submit.prevent="handleSubmit" class="form-container">
        <div class="left-side">
          <div class="form-group-f">
            <label for="nome">Nome</label>
            <input id="nome" v-model="fornecedor.nome" required />
          </div>

          <div class="form-group-f">
            <label for="email">E-mail</label>
            <input id="email" type="email" v-model="fornecedor.email" required />
          </div>

          <div class="form-group-f">
            <label for="cep">CEP</label>
            <input
              id="cep"
              v-model="fornecedor.cep"
              @input="mascararCep"
              required
              placeholder="00000-000"
            />
          </div>

          <div class="form-group-f">
            <label>Tipo de Pessoa</label>
            <div class="checkbox-group">
              <label>
                <input type="radio" value="PF" v-model="fornecedor.tipo" name="tipoPessoa" />
                Pessoa Física
              </label>
              <label>
                <input type="radio" value="PJ" v-model="fornecedor.tipo" name="tipoPessoa" />
                Pessoa Jurídica
              </label>
            </div>
          </div>

          <div class="form-group-f">
            <label :for="fornecedor.tipo === 'PJ' ? 'cnpj' : 'cpf'">
              {{ fornecedor.tipo === 'PJ' ? 'CNPJ' : 'CPF' }}
            </label>
            <input
              :id="fornecedor.tipo === 'PJ' ? 'cnpj' : 'cpf'"
              v-model="fornecedor.documento"
              @input="mascararDocumento"
              required
              :placeholder="fornecedor.tipo === 'PJ' ? '00.000.000/0000-00' : '000.000.000-00'"
            />
          </div>

          <div class="form-group-f" v-if="fornecedor.tipo === 'PF'">
            <label for="rg">RG</label>
            <input
              id="rg"
              v-model="fornecedor.rg"
              @input="mascararRg"
              required
              placeholder="00.000.000-0"
            />
          </div>

          <div class="form-group-f" v-if="fornecedor.tipo === 'PF'">
            <label for="dataNascimento">Data de Nascimento</label>
            <input id="dataNascimento" type="date" v-model="fornecedor.dataNascimento" required />
          </div>
        </div>

        <div class="right-side">
          <h2 style="margin-bottom: 0">Empresas</h2>
          <div class="empresas-table-wrapper">
            <table class="empresas-table">
              <thead>
                <tr>
                  <th></th>
                  <th>Nome</th>
                  <th>CNPJ</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="empresa in empresas" :key="empresa.id">
                  <td>
                    <input type="checkbox" :value="empresa.id" v-model="empresasSelecionadas" />
                  </td>
                  <td>{{ empresa.nomeFantasia }}</td>
                  <td>{{ formatarCNPJ(empresa.cnpj) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div class="button-group">
          <button type="submit">Cadastrar</button>
          <button type="button" class="btn-clear" @click="limparCampos">Limpar</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import Header from '@/components/Header.vue'
import { useToast } from 'vue-toastification'
import {
  limparMascara,
  mascararCep as mascararCepFn,
  mascararDocumento as mascararDocumentoFn,
  mascararRg as mascararRgFn,
  formatarCNPJ,
} from '@/utils/mascaraInput'
import { validarCPF, validarCNPJ } from '@/utils/validador'
import '../styles/cadFornecedor.css'

const toast = useToast()

const fornecedor = ref({
  nome: '',
  tipo: 'PJ',
  documento: '',
  email: '',
  cep: '',
  rg: '',
  dataNascimento: '',
})

const empresas = ref<any[]>([])
const empresasSelecionadas = ref<number[]>([])

onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/empresa')
    empresas.value = response.data
  } catch (error) {
    toast.error('Erro ao carregar empresas')
    console.error(error)
  }
})

watch(
  () => fornecedor.value.tipo,
  () => {
    fornecedor.value.documento = ''
    fornecedor.value.rg = ''
    fornecedor.value.dataNascimento = ''
  },
)

function mascararCep() {
  fornecedor.value.cep = mascararCepFn(fornecedor.value.cep)
}

function mascararDocumento() {
  fornecedor.value.documento = mascararDocumentoFn(
    fornecedor.value.documento,
    fornecedor.value.tipo,
  )
}

function mascararRg() {
  fornecedor.value.rg = mascararRgFn(fornecedor.value.rg)
}

function limparCampos() {
  fornecedor.value = {
    nome: '',
    tipo: 'PJ',
    documento: '',
    email: '',
    cep: '',
    rg: '',
    dataNascimento: '',
  }
  empresasSelecionadas.value = []
  toast.info('Todos os campos foram limpos!')
}

async function handleSubmit() {
  console.log(empresasSelecionadas)
  const doc = limparMascara(fornecedor.value.documento)
  const cep = limparMascara(fornecedor.value.cep)
  const rg = limparMascara(fornecedor.value.rg)

  if (!fornecedor.value.nome || !fornecedor.value.email || !doc || !cep) {
    toast.error('Preencha todos os campos obrigatórios.')
    return
  }

  if (fornecedor.value.tipo === 'PJ' && !validarCNPJ(doc)) {
    toast.error('CNPJ inválido.')
    return
  }

  if (fornecedor.value.tipo === 'PF') {
    if (!validarCPF(doc)) {
      toast.error('CPF inválido.')
      return
    }
    if (!rg || !fornecedor.value.dataNascimento) {
      toast.error('Preencha RG e data de nascimento.')
      return
    }
  }

  const payload = {
    nome: fornecedor.value.nome,
    tipoPessoa: fornecedor.value.tipo,
    documento: doc,
    email: fornecedor.value.email,
    cep: cep,
    rg: fornecedor.value.tipo === 'PF' ? rg : null,
    dataNascimento: fornecedor.value.tipo === 'PF' ? fornecedor.value.dataNascimento : null,
    empresas: empresasSelecionadas.value,
  }

  try {
    await axios.post('http://localhost:8080/api/fornecedor', payload)
    toast.success('Fornecedor cadastrado com sucesso!')
    limparCampos()
  } catch (error) {
    toast.error('Erro ao cadastrar fornecedor.')
    console.error(error)
  }
}
</script>
