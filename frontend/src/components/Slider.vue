<template>
  <section class="slider">
    <div class="slides" :style="{ transform: `translateX(-${currentIndex * 100}vw)` }">
      <img v-for="(image, index) in images" :key="index" :src="image" :alt="`Slide ${index + 1}`">
    </div>
    
    <div class="slide-buttons">
      <button @click="prevSlide">⬅</button>
      <button @click="nextSlide">➡</button>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'


const currentIndex = ref(0)
const totalSlides = ref(5)
let slideInterval = null

const images = [
  '/images/banner/1.jpg',
  '/images/banner/2.jpg',
  '/images/banner/3.jpg',
  '/images/banner/4.jpg',
  '/images/banner/5.jpg'
]

const showSlide = () => {
  // The transform is handled by the template binding
}

const nextSlide = () => {
  currentIndex.value = (currentIndex.value + 1) % totalSlides.value
  showSlide()
  resetTimer()
}

const prevSlide = () => {
  currentIndex.value = (currentIndex.value - 1 + totalSlides.value) % totalSlides.value
  showSlide()
  resetTimer()
}

const autoSlide = () => {
  nextSlide()
}

const resetTimer = () => {
  clearInterval(slideInterval)
  slideInterval = setInterval(autoSlide, 2000)
}

onMounted(() => {
  slideInterval = setInterval(autoSlide, 2000)
})

onUnmounted(() => {
  if (slideInterval) {
    clearInterval(slideInterval)
  }
})
</script>
