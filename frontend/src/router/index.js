import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
        path: '/signup',
        name: 'SignUp',
        component: () => import('../views/SignUpForm.vue')
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/LoginForm.vue')
    },
    {
        path: '/products',
        name: 'ProductAll',
        component: () => import('../views/ProductAll.vue')
    },
    {
        path: '/account',
        name: 'Account',
        component: () => import('../views/AccountManagement.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) return savedPosition
        return { left: 0, top: 0 }
    }
})

export default router
