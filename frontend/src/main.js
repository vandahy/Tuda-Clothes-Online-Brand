import { createApp } from 'vue'
import './style.css'
import router from './router'
import App from './App.vue'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

const app = createApp(App)

app.use(router).mount('#app')
