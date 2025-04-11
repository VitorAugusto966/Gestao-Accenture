<template>
  <div>
    <Header />
    <div class="container-f-edit">
      <h1>Editar Fornecedor</h1>
      <form @submit.prevent="handleUpdate" class="form-container-edit">
        <div class="left-side-edit">
          <div class="form-group-f-edit">
            <label for="nome">Nome</label>
            <input id="nome" v-model="fornecedor.nome" required />
          </div>

          <div class="form-group-f-edit">
            <label for="email">E-mail</label>
            <input id="email" type="email" v-model="fornecedor.email" required />
          </div>

          <div class="form-group-f-edit">
            <label for="cep">CEP</label>
            <input
              id="cep"
              :value="fornecedor.cep"
              @input="aplicarMascaraCep"
              required
              placeholder="00000-000"
            />
          </div>

          <div class="form-group-f-edit">
            <label>Tipo de Pessoa</label>
            <div class="checkbox-group-edit">
              <label>
                <input type="radio" value="PF" v-model="fornecedor.tipoPessoa" name="tipoPessoa" />
                Pessoa Física
              </label>
              <label>
                <input type="radio" value="PJ" v-model="fornecedor.tipoPessoa" name="tipoPessoa" />
                Pessoa Jurídica
              </label>
            </div>
          </div>

          <div class="form-group-f-edit">
            <label :for="fornecedor.tipoPessoa === 'PJ' ? 'cnpj' : 'cpf'">
              {{ fornecedor.tipoPessoa === 'PJ' ? 'CNPJ' : 'CPF' }}
            </label>
            <input
              :id="fornecedor.tipoPessoa === 'PJ' ? 'cnpj' : 'cpf'"
              :value="fornecedor.documento"
              @input="aplicarMascaraDocumento"
              required
              :placeholder="
                fornecedor.tipoPessoa === 'PJ' ? '00.000.000/0000-00' : '000.000.000-00'
              "
            />
          </div>

          <div class="form-group-f-edit" v-if="fornecedor.tipoPessoa === 'PF'">
            <label for="rg">RG</label>
            <input
              id="rg"
              :value="fornecedor.rg"
              @input="aplicarMascaraRg"
              required
              placeholder="00.000.000-0"
            />
          </div>

          <div class="form-group-f-edit" v-if="fornecedor.tipoPessoa === 'PF'">
            <label for="dataNascimento">Data de Nascimento</label>
            <input id="dataNascimento" type="date" v-model="fornecedor.dataNascimento" required />
          </div>
        </div>

        <div class="right-side-edit">
          <h2 style="margin-bottom: 0">Empresas</h2>
          <div class="empresas-table-wrapper-edit">
            <table class="empresas-table-edit">
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

        <div class="button-group-edit">
          <button type="submit">Salvar Alterações</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
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
import '../styles/editFornecedor.css'

const toast = useToast()
const router = useRouter()

const fornecedor = ref(JSON.parse(localStorage.getItem('fornecedor') || '{}'))
const empresas = ref<any[]>([])
const empresasSelecionadas = ref<number[]>(fornecedor.value.empresas || [])

onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/empresa')
    empresas.value = response.data
  } catch (error) {
    toast.error('Erro ao carregar empresas')
    console.error(error)
  }
})

function aplicarMascaraCep(event: Event) {
  const target = event.target as HTMLInputElement
  const valorMascarado = mascararCepFn(target.value)
  target.value = valorMascarado
  fornecedor.value.cep = valorMascarado
}

function aplicarMascaraDocumento(event: Event) {
  const target = event.target as HTMLInputElement
  const valorMascarado = mascararDocumentoFn(target.value, fornecedor.value.tipo)
  target.value = valorMascarado
  fornecedor.value.documento = valorMascarado
}

function aplicarMascaraRg(event: Event) {
  const target = event.target as HTMLInputElement
  const valorMascarado = mascararRgFn(target.value)
  target.value = valorMascarado
  fornecedor.value.rg = valorMascarado
}

async function handleUpdate() {
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
    id: fornecedor.value.id,
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
    await axios.put(`http://localhost:8080/api/fornecedor/${payload.id}`, payload)
    toast.success('Fornecedor atualizado com sucesso!')
    router.push('/fornecedores')
  } catch (error) {
    toast.error('Erro ao atualizar fornecedor.')
    console.error(error)
  }
}
</script>
