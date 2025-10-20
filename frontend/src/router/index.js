import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import UserManager from "../views/UserManager.vue";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/UserManager',
    name: 'UserManager',
    component: UserManager
  },


  {
    path: '/signup',
    name: 'SignUp',
    component: () => import('../views/SignUpForm.vue')
  },
  {
    path: '/order-detail/:id',
    name: 'order-detail',
    component: () => import('../views/OrderDetail.vue')
  },
  {
    path: '/my-OrderSuccess',
    name: 'OrderSuccess',
    component: () => import('../views/OrderSuccess.vue')
  },
  {
    path: '/order-form',
    name: 'order-form',
    component: () => import('../views/OrderForm.vue')
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
