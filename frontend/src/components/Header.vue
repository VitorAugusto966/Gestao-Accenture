<template>
  <header class="header">
    <nav class="nav">
      <div class="dropdown">
        <button class="dropbtn" @click="toHome">Home</button>
      </div>

      <div class="dropdown">
        <button class="dropbtn" @click="toggleFornecedor">Fornecedor</button>
        <div v-if="showFornecedor" class="dropdown-content">
          <RouterLink to="/fornecedor/cad" @click="closeDropdowns">Cadastrar</RouterLink>
          <RouterLink to="/fornecedores" @click="closeDropdowns">Lista de fornecedores</RouterLink>
        </div>
      </div>

      <div class="dropdown">
        <button class="dropbtn" @click="toggleEmpresa">Empresa</button>
        <div v-if="showEmpresa" class="dropdown-content">
          <RouterLink to="/empresa/cad" @click="closeDropdowns">Cadastrar</RouterLink>
          <RouterLink to="/empresas" @click="closeDropdowns">Lista de empresas</RouterLink>
        </div>
      </div>
    </nav>

    <div class="logo" @click="toHome">Accenture</div>
  </header>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const showFornecedor = ref(false)
const showEmpresa = ref(false)

function toggleFornecedor() {
  showFornecedor.value = !showFornecedor.value
  showEmpresa.value = false
}

function toggleEmpresa() {
  showEmpresa.value = !showEmpresa.value
  showFornecedor.value = false
}

function closeDropdowns() {
  showFornecedor.value = false
  showEmpresa.value = false
}

function toHome() {
  router.push('/')
}

function handleClickOutside(e) {
  if (!e.target.closest('.dropdown')) {
    closeDropdowns()
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 1rem 2rem;
  background: linear-gradient(to right, #007bff, #0056b3);
  color: white;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  min-width: 100vh;
}

.nav {
  display: flex;
  gap: 2rem;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  margin-left: auto;
  cursor: pointer;
}

.dropdown {
  position: relative;
}

.dropbtn {
  background: none;
  border: none;
  color: white;
  font-size: 1rem;
  cursor: pointer;
  font-weight: 500;
}

.dropdown-content {
  position: absolute;
  top: 2.5rem;
  left: 0;
  background-color: white;
  min-width: 150px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
  z-index: 10;
  display: flex;
  flex-direction: column;
}

.dropdown-content a {
  color: #007bff;
  padding: 0.75rem 1rem;
  text-decoration: none;
  font-weight: 500;
  transition: background 0.2s;
}

.dropdown-content a:hover {
  background-color: #f1f1f1;
}
</style>
