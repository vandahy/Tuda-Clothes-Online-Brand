import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";

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
  },
  {
    path: "/products/:code",
    name: "ProductDetail",
    component: () => import("../views/ProductDetail.vue"),
    props: true,
  }
]


const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      // Khi dùng nút back/forward sẽ cuộn lại vị trí cũ
      return savedPosition;
    } else {
      // Khi điều hướng sang trang mới thì cuộn về đầu
      return { top: 0 };
    }
  },
});

export default router;
