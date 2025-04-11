<template>
  <main>
    <Header />

    <section class="home-welcome">
      <h1>👋 Bem-vindo ao Sistema de Gestão</h1>
      <p>Gerencie empresas e fornecedores de forma prática</p>
    </section>

    <section class="quick-access">
      <div class="card" @click="goTo('/fornecedores')">
        <div class="icon">👨‍💼</div>
        <h2>Fornecedores</h2>
        <p>Cadastro, edição, exclusão e visualização de fornecedores.</p>
      </div>

      <div class="card" @click="goTo('/empresas')">
        <div class="icon">🏢</div>
        <h2>Empresas</h2>
        <p>Cadastro completo de empresas e vínculo com fornecedores.</p>
      </div>
    </section>
  </main>
</template>

<script setup lang="ts">
import Header from '@/components/Header.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

function goTo(path: string) {
  router.push(path)
}
</script>

<style scoped>
main {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f8fafc;
  min-height: 100vh;
}

.home-welcome {
  text-align: center;
  margin-top: 3rem;
  color: #003366;
}

.home-welcome h1 {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.home-welcome p {
  font-size: 1.2rem;
  color: #555;
  max-width: 600px;
  margin: 0 auto;
}

.quick-access {
  display: flex;
  justify-content: center;
  gap: 2rem;
  margin: 4rem auto;
  padding: 0 1rem;
  max-width: 1000px;
  flex-wrap: wrap;
}

.card {
  background-color: #ffffff;
  padding: 2rem;
  border-radius: 16px;
  width: 280px;
  text-align: center;
  cursor: pointer;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.06);
  transition:
    transform 0.2s ease,
    background-color 0.3s ease;
  border-top: 6px solid #007bff;
}

.card:hover {
  transform: translateY(-6px);
  background-color: #eef7ff;
}

.card h2 {
  color: #0056b3;
  margin: 1rem 0 0.5rem;
  font-size: 1.4rem;
}

.card p {
  color: #444;
  font-size: 1rem;
}

.icon {
  font-size: 2.5rem;
  margin-bottom: 0.8rem;
}
</style>
