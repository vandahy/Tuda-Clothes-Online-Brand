<template>
  <section class="products" id="products-id">
    <h1 id="product-title">NEW ARRIVALS</h1>
    <div class="product-carousel">
      <div
        class="product-container"
        :style="{ transform: `translateX(-${currentIndex * slideWidth}%)` }"
      >
        <div
          class="product-item"
          v-for="(product, index) in products"
          :key="index"
        >
          <div class="image-container">
            <img
              :src="product.defaultImage"
              :alt="product.name"
              class="default-img"
            />
            <img
              :src="product.hoverImage"
              :alt="product.name"
              class="hover-img"
            />
          </div>
          <h2>{{ product.name }}</h2>
          <h4>{{ product.price }}</h4>
        </div>
      </div>
    </div>
    <router-link to="/products">
      <button class="more-btn">See more</button>
    </router-link>
  </section>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const currentIndex = ref(0);
const totalProducts = ref(15);
const slideWidth = ref(18); // 18% cho desktop, sẽ được cập nhật cho mobile
let carouselInterval = null;

const products = ref([
  {
    name: "Product 1",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 2",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 3",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 4",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 5",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 6",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 7",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 8",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 9",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 10",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 11",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 12",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 13",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 14",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
  {
    name: "Product 15",
    price: "0,00$",
    defaultImage: "/src/assets/images/products/tsh1.jpg",
    hoverImage: "/src/assets/images/products/tsh1hover.jpg",
  },
]);

const nextSlide = () => {
  // Chỉ trượt khi còn đủ sản phẩm để hiển thị
  if (currentIndex.value < totalProducts.value - 5) {
    currentIndex.value += 1;
  } else {
    currentIndex.value = 0; // Quay về đầu khi hết sản phẩm
  }
};

const updateSlideWidth = () => {
  slideWidth.value = window.innerWidth <= 768 ? 50 : 20; // Mobile: 50% (48% width + 2% margin), Desktop: 20% (18% width + 2% margin)
};

onMounted(() => {
  updateSlideWidth();
  window.addEventListener("resize", updateSlideWidth);
  carouselInterval = setInterval(nextSlide, 10000); // 10 giây
});

onUnmounted(() => {
  if (carouselInterval) {
    clearInterval(carouselInterval);
  }
  window.removeEventListener("resize", updateSlideWidth);
});
</script>
