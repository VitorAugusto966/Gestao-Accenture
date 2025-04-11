import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CadEmpresaView from '@/views/CadEmpresaView.vue'
import CadFornecedorView from '@/views/CadFornecedorView.vue'
import ListEmpresaView from '@/views/ListEmpresaView.vue'
import ListFornecedorView from '@/views/ListFornecedorView.vue'
import EditEmpresaView from '@/views/EditEmpresaView.vue'
import EditFornecedorView from '@/views/EditFornecedorView.vue'
import VincFornecedorEmpresa from '@/views/VincFornecedorEmpresa.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/empresa/cad',
      name: 'Cadastro Empresa',
      component: CadEmpresaView,
    },
    {
      path: '/empresa/editar',
      name: 'Editar Empresa',
      component: EditEmpresaView,
    },
    {
      path: '/empresa/vincular',
      name: 'Vincular empresa',
      component: VincFornecedorEmpresa,
    },
    {
      path: '/empresas',
      name: 'Listagem de Empresas',
      component: ListEmpresaView,
    },
    {
      path: '/fornecedor/cad',
      name: 'Cadastro Fornecedor',
      component: CadFornecedorView,
    },
    {
      path: '/fornecedor/editar',
      name: 'Editar Fornecedor',
      component: EditFornecedorView,
    },
    {
      path: '/fornecedores',
      name: 'Listagem de Fornecedores',
      component: ListFornecedorView,
    },
  ],
})

export default router
