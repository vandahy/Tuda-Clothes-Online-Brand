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
    path: '/category-manager',
    name: 'CategoryManager',
    component: () => import('../views/management/CategoryManager.vue'),
    meta: { requiresAuth: true, roles: ['ADMIN', 'FOUNDER'] }
  },
  {
    path: '/product-manager',
    name: 'ProductManager',
    component: () => import('../views/management/ProductManager.vue'),
    meta: { requiresAuth: true, roles: ['ADMIN', 'FOUNDER', 'EMPLOYEE'] }
  },
  {
    path: '/user-manager',
    name: 'UserManagerPage',
    component: () => import('../views/management/UserManager.vue'),
    meta: { requiresAuth: true, roles: ['ADMIN', 'FOUNDER', 'EMPLOYEE'] }
  },
  {
    path: '/order-manager',
    name: 'OrderManager',
    component: () => import('../views/management/OrderManager.vue'),
    meta: { requiresAuth: true, roles: ['ADMIN', 'FOUNDER', 'EMPLOYEE'] }
  },


  {
    path: '/signup',
    name: 'SignUp',
    component: () => import('../views/SignUpForm.vue')
  },
  {
    path: '/order-detail',
    name: 'order-detail',
    component: () => import('../views/OrderDetail.vue')
  },
  {
    path: '/my-OrderSuccess/:orderId',
    name: 'OrderSuccess',
    component: () => import('../views/OrderSuccess.vue'),
    props: true
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
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
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

// Navigation guard để kiểm tra quyền truy cập
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const userRole = localStorage.getItem('userRole');
  
  // Kiểm tra nếu route yêu cầu authentication
  if (to.meta.requiresAuth) {
    if (!token) {
      // Chưa đăng nhập -> chuyển về login
      alert('Please login to access this page');
      next('/login');
      return;
    }
    
    // Kiểm tra role nếu route có yêu cầu
    if (to.meta.roles && !to.meta.roles.includes(userRole)) {
      alert('You do not have permission to access this page');
      next('/'); // Chuyển về trang chủ
      return;
    }
  }
  
  next(); // Cho phép truy cập
});

export default router;
