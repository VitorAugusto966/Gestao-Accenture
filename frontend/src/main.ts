import './assets/main.css'
import Toast, { POSITION } from 'vue-toastification'
import 'vue-toastification/dist/index.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(router)
app.use(Toast, {
  position: POSITION.TOP_RIGHT,
  toastClassName: 'custom-toast',
  containerClassName: 'custom-toast-container',
  hideProgressBar: false,
  timeout: 3000,
  closeOnClick: true,
  pauseOnHover: true,
  draggable: true,
  draggablePercent: 0.6,
  showCloseButtonOnHover: false,
})

app.mount('#app')
