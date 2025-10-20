<template>
  <header>

    <nav class="navbar">
      <router-link to="/">
        <img class="logo w-1/3 flex justify-center" src="/src/assets/images/logo.png" alt="Logo">
      </router-link>
      <ul class="navbar-ul">
        <li class="dropdown">
          <a class="a-nav" href="">SHOP</a>
          <ul class="submenu">
            <li>
              <router-link class="a-nav" to="/products">All</router-link>
            </li>
            <li v-for="category in categories" :key="category.categoryCode">
              <router-link class="a-nav" :to="`/products?category=${category.categoryCode}`">
                {{ category.name }}
              </router-link>
            </li>
          </ul>
        </li>
        <li><a class="a-nav" id="nav-products" href="#products-id">NEW ARRIVALS</a></li>
        <li><a class="a-nav" href="">NEWS</a></li>
        <li><a class="a-nav" href="">CONTACT US</a></li>
      </ul>    
    </nav>

   <!-- <div class="menu-icon flex">
  <i class="fa-solid fa-cart-shopping c515151 menu-icon-btn" @click="toggleSidebarCart"></i>
  <i class="fa-solid fa-user c515151 menu-icon-btn" @click="goToUserPage"></i>
  <i class="fa-solid fa-bars menu-icon-btn" @click="toggleSidebar"></i>
  <i class="fa-solid fa-gear menu-icon-btn" @click="toggleManager"></i>
</div> -->
<div class="menu-icon">
  <i class="fa-solid fa-cart-shopping menu-icon-btn" @click="toggleSidebarCart"></i>
  <i class="fa-solid fa-user menu-icon-btn" @click="goToUserPage"></i>
  <i class="fa-solid fa-bars menu-icon-btn" @click="toggleSidebar"></i>
  <i class="fa-solid fa-gear menu-icon-btn" @click="toggleManager"></i>
</div>
<!-- Manager Dropdown -->
<div v-if="Manager" class="manager-dropdown">
  <router-link class="manager-item" to="/OrderManager">Order</router-link>
  <router-link class="manager-item" to="/ProductManager">Product</router-link>
  <router-link class="manager-item" to="/CategoryManager">Category</router-link>
  <router-link class="manager-item" to="/UserManager">User</router-link>
</div>


    <!-- Sidebar Menu -->
    <div class="sidebar" id="sidebar" :class="{ active: sidebarActive }">
      <div class="close-btn" id="closeBtn" @click="closeSidebar">
        <i class="fas fa-times"></i>
      </div>
      <ul>
        <li>
          <a id="menu-item" class="a-menu" href="javascript:void(0)" @click="toggleSubmenu">SHOP</a>
          <ul class="submenu-slidebar" id="submenu" :class="{ active: submenuActive }">
            <li>
              <router-link class="a-menu child" to="/products">All</router-link>
            </li>
            <li v-for="category in categories" :key="category.categoryCode">
              <router-link class="a-menu child" :to="`/products?category=${category.categoryCode}`">
                {{ category.name }}
              </router-link>
            </li>
          </ul>
        </li>
        <li><a class="a-menu" href="">NEW ARRIVALS</a></li>
        <li><a class="a-menu" href="">NEWS</a></li>
        <li><a class="a-menu" href="">CONTACT US</a></li>
      </ul>
    </div>
    <!-- Sidebar Menu -->

    <!-- Sidebar Cart -->
    <div class="cart sidebar c-black" id="sidebar" :class="{ active: sidebarCart }">
       <div class="sidebar-header">
        <div class="close-btn" id="closeBtn" @click="closeSidebar">
          <i class="fas fa-times"></i>
        </div>
      </div>

      <h2 class="cart-title">Your Cart</h2>
      <div class="cart-content">
        <div class="cart-box">
<img src="https://content.pancake.vn/1/s700x875/b3/af/03/96/b8461d2219e55c4aaaa43b0f2d6d216133b4c96fffe015151eff03ca-w:3000-h:3750-l:971270-t:image/jpeg.jpeg" alt="Tee">
          <div class="cart-detail">
            <h2 class="cart-product-title">Casual Black Polo</h2>
            <span class="cart-price">$100</span>
            <div class="cart-quantity">
              <button class="decrement">-</button>
              <span class="number">1</span>
              <button class="increment">+</button>
            </div>
          </div>
          <i class="fas fa-trash cart-remove"></i>
        </div>
      </div>

      <div class="total-footer">
        <div class="total">
            <div class="total-title">Total</div>
            <div class="total-price">$0</div>
        </div>
        <!-- <button class="total-buy">Buy Now</button> -->
         <router-link to="/order-form" @click="closeSidebar" class="total-buy">Buy Now</router-link>
    </div>
    </div>
    <!-- Sidebar Cart -->
  </header>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue' 
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const sidebarActive = ref(false)
const sidebarCart = ref(false)
const submenuActive = ref(false)
const Manager = ref(false) // <-- thêm state cho menu manager
const categories = ref([]) 

// Toggle sidebar / cart / manager menu
const toggleSidebar = () => sidebarActive.value = !sidebarActive.value
const toggleSidebarCart = () => sidebarCart.value = !sidebarCart.value
const toggleManager = () => Manager.value = !Manager.value
const closeSidebar = () => { sidebarActive.value = false; sidebarCart.value = false }
const toggleSubmenu = () => submenuActive.value = !submenuActive.value

// Fetch categories
onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/categories')
    categories.value = response.data
  } catch (error) {
    console.error('Error fetching categories:', error)
  }
})

// Smooth scroll
const scrollToProducts = () => {
  const target = document.getElementById('products-id')
  if (target) target.scrollIntoView({ behavior: 'smooth' })
}

// Cart quantity buttons
nextTick(() => {
  document.querySelectorAll('.cart-box').forEach(cartBox => {
    const numberElement = cartBox.querySelector(".number")
    const decrementBtn = cartBox.querySelector(".decrement")
    const incrementBtn = cartBox.querySelector(".increment")

    decrementBtn.addEventListener("click", () => {
      let quantity = parseInt(numberElement.textContent)
      if (quantity > 1) {
        quantity--
        numberElement.textContent = quantity
        decrementBtn.style.color = quantity === 1 ? "#999" : "#333"
      }
    })

    incrementBtn.addEventListener("click", () => {
      let quantity = parseInt(numberElement.textContent)
      quantity++
      numberElement.textContent = quantity
      decrementBtn.style.color = "#333"
    })
  })
})

// Check login
const goToUserPage = () => {
  const loggedIn = localStorage.getItem("userLoggedIn")
  router.push(loggedIn ? "/account" : "/login")
}

// Click ngoài để đóng menu manager
document.addEventListener("click", (e) => {
  const managerMenu = document.querySelector(".manager-dropdown")
  const managerIcon = document.querySelector(".fa-people-roof")
  if (Manager.value && managerMenu && !managerMenu.contains(e.target) && !managerIcon.contains(e.target)) {
    Manager.value = false
  }
})
</script>
<style scoped>
.menu-icon {
  display: flex;
  align-items: center;       /* căn thẳng hàng với navbar */
  gap: 1.5rem;               /* khoảng cách giữa các icon */
  margin-left: auto;          /* đẩy sang phải */
}

.menu-icon-btn {
  cursor: pointer;
  font-size: 1.3rem;
  color: #515151;
  transition: color 0.2s;
}

.menu-icon-btn:hover {
  color: #000;
}

/* Manager dropdown style */
.manager-dropdown {
  position: absolute;
  top: 100%;                 /* ngay dưới icon */
  right: 0;
  background: white;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.2);
  display: flex;
  flex-direction: column;
  z-index: 1000;
}

.manager-item {
  padding: 0.5rem 1rem;
  text-decoration: none;
  color: #333;
}

.manager-item:hover {
  background-color: #f0f0f0;
}

</style>
