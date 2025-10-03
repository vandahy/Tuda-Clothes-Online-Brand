<template>
  <section class="products" id="products-id">
    <h1 id="product-title">NEW ARRIVALS</h1>
    <div class="product-carousel">
      <!-- Nút điều hướng trái -->
      <button class="carousel-btn prev-btn" @click="prevSlide">&#8592;</button>
      <div
        class="product-container"
        :style="{
          transform: `translateX(-${currentIndex * 20}%)`
        }"
      >
        <div
          class="product-item"
          v-for="product in products"
          :key="product.productCode"
          @click="$router.push({ name: 'ProductDetail', params: { code: product.productCode } })"
        >
          <div class="image-container">
            <img
              :src="product.imageUrl || '/src/assets/images/products/tsh1.jpg'"
              :alt="product.name"
              class="default-img"
            />
          </div>
          <h2>{{ product.name }}</h2>
          <h4>{{ formatPrice(product.price) }}</h4>
        </div>
      </div>
      <!-- Nút điều hướng phải -->
      <button class="carousel-btn next-btn" @click="nextSlide">&#8594;</button>
    </div>
    <router-link to="/products">
      <button class="more-btn">See more</button>
    </router-link>
  </section>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const products = ref([]);
const currentIndex = ref(0);
const slideCount = 5;
let carouselInterval = null;

const fetchProducts = async () => {
  try {
    const res = await fetch(`/api/products/new?page=0&size=20`);
    const data = await res.json();
    products.value = Array.isArray(data) ? data : data.content || [];
  } catch (e) {
    products.value = [];
  }
};

const nextSlide = () => {
  if (products.value.length <= slideCount) return;
  if (currentIndex.value < products.value.length - slideCount) {
    currentIndex.value += 1;
  } else {
    currentIndex.value = 0;
  }
};

const prevSlide = () => {
  if (products.value.length <= slideCount) return;
  if (currentIndex.value > 0) {
    currentIndex.value -= 1;
  } else {
    currentIndex.value = products.value.length - slideCount;
  }
};

const formatPrice = (price) => {
  if (!price) return "0₫";
  return Number(price).toLocaleString("vi-VN", {
    style: "currency",
    currency: "VND",
  });
};

onMounted(() => {
  fetchProducts();
  carouselInterval = setInterval(nextSlide, 5000);
});
onUnmounted(() => {
  if (carouselInterval) clearInterval(carouselInterval);
});
</script>

<style scoped>
.product-carousel {
  overflow: hidden;
  width: 100%;
  margin: 0 auto;
  position: relative;
}

.product-container {
  display: flex;
  transition: transform 0.5s;
}

.product-item {
  background: #f7f7f7;
  border-radius: 10px;
  text-align: center;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.05);
  min-width: 0;
}

.product-item img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px;
}

.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 10;
  background: rgba(255, 255, 255, 0.8);
  border: none;
  font-size: 2rem;
  cursor: pointer;
  padding: 0 10px;
  border-radius: 50%;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: background 0.2s;
}

.prev-btn {
  left: 0;
}

.next-btn {
  right: 0;
}

.carousel-btn:hover {
  background: #e0e0e0;
}

@media (max-width: 1024px) {
  .product-item {
    flex: 0 0 33.33%;
  }
}
@media (max-width: 600px) {
  .product-item {
    flex: 0 0 50%;
  }
}
</style>
