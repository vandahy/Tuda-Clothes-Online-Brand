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
      <i class="fa-solid fa-cart-shopping c515151" id="menu-icon" @click="toggleSidebar"></i>
      <router-link to="/login">
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
    
      <!-- Nút Giỏ Hàng -->
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'

const sidebarActive = ref(false)
const submenuActive = ref(false)

const toggleSidebar = () => {
  sidebarActive.value = !sidebarActive.value
}

const closeSidebar = () => {
  sidebarActive.value = false
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
