<template>
  <header>
    <nav class="navbar">
      <a href="/">
        <img class="logo w-1/3 flex justify-center" src="/src/assets/images/logo.png" alt="Logo">
      </a>
      <ul class="navbar-ul">
        <li class="dropdown">
          <a class="a-nav" href="">SHOP</a>
          <ul class="submenu">
            <li><a href="#">All</a></li>
            <li><a href="#">T-Shirts</a></li>
            <li><a href="#">Pants</a></li>
            <li><a href="#">Outerwears</a></li>
            <li><a href="#">Accessories</a></li>
          </ul>
        </li>
        <li><a class="a-nav" id="nav-products" href="#products-id">NEW ARRIVALS</a></li>
        <li><a class="a-nav" href="">NEWS</a></li>
        <li><a class="a-nav" href="">CONTACT US</a></li>
      </ul>    
    </nav>
    <div class="menu-icon flex">
      <i class="fa-solid fa-cart-shopping c515151" id="menu-icon" @click="toggleSidebarCart"></i>
      <router-link to="/signup">
        <i class="fa-solid fa-user c515151" id="menu-icon"></i>
      </router-link>
      <i class="fa-solid fa-bars" id="menu-icon" @click="toggleSidebar"></i>
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
            <li><a class="a-menu child" href="#">All</a></li>
            <li><a class="a-menu child" href="#">T-Shirts</a></li>
            <li><a class="a-menu child" href="#">Pants</a></li>
            <li><a class="a-menu child" href="#">Outerwears</a></li>
            <li><a class="a-menu child" href="#">Accessories</a></li>
          </ul>
        </li>
        <li><a class="a-menu" href="">NEW ARRIVALS</a></li>
        <li><a class="a-menu" href="">NEWS</a></li>
        <li><a class="a-menu" href="">CONTACT US</a></li>
      </ul>
    </div>

    <!-- Sidebar Cart -->
    <div class="sidebar-cart sidebar c-black" id="sidebar" :class="{ active: sidebarCart }">
      <div class="sidebar-header">
        <span>Giỏ hàng của bạn</span>
        <div class="close-btn" id="closeBtn" @click="closeSidebar">
        <i class="fas fa-times"></i>
      </div>
      </div>

      <!-- Item 1 -->
      <div class="cart-item">
        <img src="https://levents.asia/cdn/shop/products/white.png?v=1" alt="Tee">
        <div class="cart-info">
          <h4>Levents® Seasonal Long Sleeve Boxy Tee</h4>
          <p>600.000 VND</p>
          <p>Size: 1 | Color: White</p>
          <div class="quantity">
            <button>-</button>
            <span>1</span>
            <button>+</button>
          </div>
        </div>
      </div>

      <!-- Item 2 -->
      <div class="cart-item">
        <img src="https://levents.asia/cdn/shop/products/blue.png?v=1" alt="Shirt">
        <div class="cart-info">
          <h4>Levents® Striped Long Sleeve Boxy Polo Shirt</h4>
          <p>650.000 VND</p>
          <p>Size: 3 | Color: Blue</p>
          <div class="quantity">
            <button>-</button>
            <span>1</span>
            <button>+</button>
          </div>
        </div>
      </div>

      <!-- Checkout -->
      <div class="checkout">
        Thanh toán 1.250.000 VND
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'

const sidebarActive = ref(false)
const sidebarCart = ref(false)
const submenuActive = ref(false)

const toggleSidebar = () => {
  sidebarActive.value = !sidebarActive.value
}

const toggleSidebarCart = () => {
  sidebarCart.value = !sidebarCart.value
}

const closeSidebar = () => {
  sidebarActive.value = false
  sidebarCart.value = false
}

const toggleSubmenu = () => {
  submenuActive.value = !submenuActive.value
}

// Smooth scroll functionality
const scrollToProducts = () => {
  const target = document.getElementById('products-id')
  if (target) {
    target.scrollIntoView({ behavior: 'smooth' })
  }
}
</script>
