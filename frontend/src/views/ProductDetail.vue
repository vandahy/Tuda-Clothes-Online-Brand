<template>
  <div class="product-detail-container">
    <div class="product-images">
      <div class="thumbnail-list">
        <img
          v-for="(img, idx) in product.images"
          :key="img.imageId"
          :src="img.imageUrl"
          :class="{ active: idx === selectedImageIndex }"
          @click="selectedImageIndex = idx"
        />
      </div>
      <div class="main-image">
        <img :src="product.images[selectedImageIndex]?.imageUrl" :alt="product.name" />
      </div>
    </div>
    <div class="product-info">
      <h1>{{ product.name }}</h1>
      <div class="sku">SKU: {{ product.sku }}</div>
      <div class="price-row">
        <span class="discount" v-if="product.discount > 0"
          >-{{ Math.round((product.discount / product.price) * 100) }}%</span
        >
        <span class="price">{{
          formatPrice(product.price - product.discount)
        }}</span>
        <span class="old-price" v-if="product.discount > 0">{{
          formatPrice(product.price)
        }}</span>
      </div>
      <div class="size-row">
          <span
              v-for="size in product.sizes"
              :key="size"
              class="size-btn"
              :class="{ 'active': size === selectedSize }"
              @click="selectedSize = size"
          >
              {{ size }}
          </span>
      </div>
      <div class="quantity-row">
        <button @click="decreaseQty">-</button>
        <input type="number" v-model.number="quantity" min="1" />
        <button @click="increaseQty">+</button>
      </div>
      <button class="add-to-cart-btn" @click="addToCart">ADD TO CART</button>
      <div class="desc-title">Description</div>
      <div class="desc-content">{{ product.description }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import api from '@/utils/api.js'; 
import emitter from '@/utils/emitter.js';

const route = useRoute();
const product = ref({
  name: "",
  sku: "",
  price: 0,
  discount: 0,
  images: [],
  sizes: [],
  description: "",
});
const selectedImageIndex = ref(0);
const quantity = ref(1);
const selectedSize = ref(null);

const fetchProduct = async () => {
  // Assume route.params.code is the product code
  const res = await fetch(`/api/products/${route.params.code}`);
  const data = await res.json();
  // Map fields properly according to your API
  product.value = {
    name: data.name,
    sku: data.productCode,
    price: data.price,
    discount: data.discount || 0,
    images: data.images || [data.imageUrl],
    sizes: data.sizes || ["S", "M", "L", "XL"],
    description: data.description,
  };
};

const increaseQty = () => quantity.value++;
const decreaseQty = () => {
  if (quantity.value > 1) quantity.value--;
};

const addToCart = async () => {
    // 1. Kiểm tra size (giữ nguyên)
    if (!selectedSize.value) {
        alert("Let select a size!");
        return;
    }

    // 2. Tạo payload (giữ nguyên)
    const cartItemPayload = {
        productCode: product.value.sku,
        quantity: quantity.value,
        size: selectedSize.value,
    };

    // 3. Gửi request bằng 'api' (đã thay thế)
    try {
        const response = await api.post('/api/carts/add', cartItemPayload);
        emitter.emit('cart-updated');
        alert(response.data.message); 

    } catch (error) {
        console.error('Errol call API add to cart', error);
        if (error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                alert('Login to add to cart!');
            } else {
                const errorMessage = error.response.data.error || error.response.data.message;
                alert(`Lỗi: ${errorMessage || 'Có lỗi xảy ra, vui lòng thử lại.'}`);
            }
        } else {
            // Lỗi mạng (mất kết nối)
            alert('Không thể kết nối đến server. Vui lòng thử lại sau.');
        }
    }
};

const formatPrice = (price) =>
    Number(price).toLocaleString("vi-VN", { style: "currency", currency: "VND" });

onMounted(() => {
    fetchProduct();
});
</script>

<style scoped>
.product-detail-container {
  display: flex;
  gap: 40px;
  background: #fff;
  border-radius: 10px;
  padding: 40px;
  margin: 100px auto;
  max-width: 1200px;
}
.product-images {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.thumbnail-list {
  display: flex;
  flex-direction: row;
  gap: 10px;
  margin-bottom: 20px;
}
.thumbnail-list img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 6px;
  cursor: pointer;
  border: 2px solid transparent;
}
.thumbnail-list img.active {
  border: 2px solid #b71c1c;
}
.main-image img {
  width: 400px;
  height: 400px;
  object-fit: contain;
  border-radius: 10px;
  background: #fafafa;
}
.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 18px;
}
.sku {
  color: #888;
  font-size: 14px;
}
.price-row {
  display: flex;
  align-items: center;
  gap: 10px;
}
.price {
  color: #b71c1c;
  font-size: 2rem;
  font-weight: bold;
}
.old-price {
  color: #888;
  text-decoration: line-through;
  font-size: 1.2rem;
}
.discount {
  color: #fff;
  background: #b71c1c;
  border-radius: 4px;
  padding: 2px 8px;
  font-size: 1rem;
  font-weight: bold;
}
.size-row {
  display: flex;
  gap: 10px;
}
.size-btn {
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 6px 16px;
  cursor: pointer;
  background: #fff;
  font-weight: bold;
}
.size-btn.active {
    background-color: black;
    color: #fff;
    border-color: black;
}
.quantity-row {
  display: flex;
  align-items: center;
  gap: 10px;
}
.quantity-row button {
  width: 32px;
  height: 32px;
  font-size: 1.2rem;
  border: 1px solid #ccc;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
}
.quantity-row input {
  width: 50px;
  text-align: center;
  font-size: 1.1rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.add-to-cart-btn {
  position: relative;
  display: inline-block;
  width: 100%;
  padding: 16px 0;
  font-size: 1.2rem;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
  color: #fff;
  background: #af041e;
  border: 1px solid #af041e;
  cursor: pointer;
  overflow: hidden;
  z-index: 1;
  transition: color 0.45s cubic-bezier(0.785, 0.135, 0.15, 0.86),
    border 0.45s cubic-bezier(0.785, 0.135, 0.15, 0.86);
}

/* background đỏ trượt vào khi hover */
.add-to-cart-btn::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: #fff;
  z-index: -1;
  transition: left 0.45s cubic-bezier(0.785, 0.135, 0.15, 0.86);
}

/* hover: chạy nền đỏ vào */
.add-to-cart-btn:hover::before {
  left: 0;
}

/* hover đổi text */
.add-to-cart-btn:hover {
  color: #af041e;
  border-color: #af041e;
}

.desc-title {
  font-weight: bold;
  margin-top: 20px;
  font-size: 1.1rem;
}
.desc-content {
  color: #222;
  font-size: 1rem;
  line-height: 1.6;
}
/* Hide spin buttons on Chrome, Safari, Edge */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Hide spin buttons on Firefox */
input[type="number"] {
  -moz-appearance: textfield; /* cho Firefox cũ */
  appearance: textfield; /* cho chuẩn hiện tại */
}
</style>