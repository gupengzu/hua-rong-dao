import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
//引入的element-plus组件
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// import './assets/main.css'

const app = createApp(App)
app.use(router)

app.use(ElementPlus).mount('#app')