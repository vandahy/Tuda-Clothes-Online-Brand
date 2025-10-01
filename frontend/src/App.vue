<template>
  <div class="container">
    <div class="head-container">
      <Navbar />
      <main>
        <Slider />
        <ProductList />
      </main>
    </div>
  </div>
  <Footer />
</template>

<script setup>
import Navbar from './components/Navbar.vue'
import Slider from './components/Slider.vue'
import ProductList from './components/ProductList.vue'
import Footer from './components/Footer.vue'
</script>