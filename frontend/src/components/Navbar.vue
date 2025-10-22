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

    <div class="menu-icon flex">
      <i class="fa-solid fa-cart-shopping c515151" id="menu-icon" @click="toggleSidebarCart"></i>
      <router-link>
        <i class="fa-solid fa-user c515151" id="menu-icon" @click="goToUserPage"></i>
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
        <router-link class="a-menu " to="/Usermanager">Usermanager</router-link>
      </ul>
    </div>
    <!-- Sidebar Menu -->

    <!-- Sidebar Cart -->
    <div class="cart sidebar c-black" id="sidebarCart" :class="{ active: sidebarCart }"> <div class="sidebar-header">
         <div class="close-btn" id="closeBtnCart" @click="closeSidebar"> <i class="fas fa-times"></i>
         </div>
       </div>

       <h2 class="cart-title">Your Cart</h2>

       <div class="cart-content">

         <div v-if="cartItems.length === 0" class="cart-empty">
            Your cart is empty
         </div>

         <div class="cart-box" v-for="(item, index) in cartItems" :key="item.id">
          <img :src="item.imageUrl || '/src/assets/images/placeholder.jpg'" :alt="item.productName">
          <div class="cart-detail">
            <h2 class="cart-product-title">{{ item.productName }} - {{ item.size }} </h2>
            <span class="cart-price">{{ formatPrice(item.price) }}</span>
            <div class="cart-quantity">
              <button class="decrement" @click="updateCartItemQuantity(item.variantId, item.quantity - 1)">-</button>
              <span class="number">{{ item.quantity }}</span>
              <button class="increment" @click="updateCartItemQuantity(item.variantId, item.quantity + 1)">+</button>
            </div>
          </div>
          <i class="fas fa-trash cart-remove" @click="removeCartItem(item.variantId)"></i>
        </div>

       </div>
       <div class="total-footer">
         <div class="total">
             <div class="total-title">Total</div>
             <div class="total-price">{{ formatPrice(cartTotal) }}</div>
         </div>
         <div class="cart-buttons flex flex-col gap-2">
        <router-link to="/order-form" @click="closeSidebar" class="total-buy">Buy Now</router-link>
      </div>
     </div>
     <div @click="cleanCart" class="clean-btn"><p>Clean All</p></div>
   </div>
    <!-- Sidebar Cart -->
  </header>
</template>

<script setup>
import { ref, onMounted, watch, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/utils/api.js'; 
import emitter from '@/utils/emitter.js';

const router = useRouter();

const sidebarActive = ref(false);
const sidebarCart = ref(false);
const submenuActive = ref(false);
const categories = ref([]);

// --- ADD CART STATE ---
const cartItems = ref([]); // To store cart items from API
const cartTotal = ref(0);  // To store calculated total price
// --- END CART STATE ---

// --- CART FUNCTIONS ---
// Function to fetch cart items from the backend
const fetchCartItems = async () => {
  // Check if user is actually logged in by checking for the token
  const token = localStorage.getItem("token");
  if (!token) {
    cartItems.value = []; // Clear cart if not logged in
    cartTotal.value = 0;
    console.log("User not logged in, clearing cart display."); // Debug log
    return;
  }

  try {
    console.log("Fetching cart items..."); // Debug log
    // Use 'api.get' which automatically sends the token
    const response = await api.get('/api/cart-items/slide-bar');
    cartItems.value = response.data;
    console.log("Cart items received:", cartItems.value); // Debug log
    calculateTotal(); // Calculate total after fetching
  } catch (error) {
    console.error('Error fetching cart items:', error);
    // Handle specific errors like 401/403 if needed
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
        console.warn("Unauthorized or Forbidden fetching cart. Maybe token expired?");
        // Optionally clear token and redirect to login
        // localStorage.removeItem("token");
        // localStorage.removeItem("userLoggedIn");
        // router.push("/login");
    }
    cartItems.value = []; // Clear cart on error
    cartTotal.value = 0;
  }
};

// Function to calculate the total price of the cart
const calculateTotal = () => {
  cartTotal.value = cartItems.value.reduce((sum, item) => {
    // Ensure 'item.price' exists and is a number.
    // This 'price' should be the TOTAL price for that item line (unit price * quantity)
    // coming directly from your DTO.
    return sum + (Number(item.price) || 0);
  }, 0);
  console.log("Calculated cart total:", cartTotal.value); // Debug log
};

// Watch for the cart sidebar opening, then fetch items
watch(sidebarCart, (newValue) => {
  if (newValue === true) {
    console.log("Cart sidebar opened, fetching items."); // Debug log
    fetchCartItems();
  } else {
    console.log("Cart sidebar closed."); // Debug log
  }
});
// --- END CART FUNCTIONS ---

// --- CATEGORY FUNCTIONS ---
// Function to fetch categories
const fetchCategories = async () => {
  try {
    // Use 'api.get' instead of axios directly
    const response = await api.get('/api/categories'); // Use relative path if baseURL is set in api.js
    categories.value = response.data;
  } catch (error) {
    console.error('Error fetching categories:', error);
  }
};
// --- END CATEGORY FUNCTIONS ---

// Lifecycle hook
onMounted(async () => {
  await fetchCategories(); // Fetch categories when component mounts
  // Optional: Fetch cart items on initial load if user might already be logged in
  // fetchCartItems();
});

// --- UI TOGGLE FUNCTIONS (Keep existing) ---
const toggleSidebar = () => {
  sidebarActive.value = !sidebarActive.value;
};

const toggleSidebarCart = () => {
  sidebarCart.value = !sidebarCart.value;
  // fetchCartItems is now handled by the 'watch' above
};

const closeSidebar = () => {
  sidebarActive.value = false;
  sidebarCart.value = false;
};

const toggleSubmenu = () => {
  submenuActive.value = !submenuActive.value;
};
// --- END UI TOGGLE FUNCTIONS ---

// --- NAVIGATION & UTILS (Keep existing, add formatPrice) ---
// Smooth scroll functionality
const scrollToProducts = () => {
  const target = document.getElementById('products-id');
  if (target) {
    target.scrollIntoView({ behavior: 'smooth' });
  }
};

// Check login status and navigate
const goToUserPage = () => {
  // Check the actual token instead of 'userLoggedIn' flag for better accuracy
  const token = localStorage.getItem("token");
  if (token) {
    router.push("/account");
  } else {
    router.push("/login");
  }
};

// Price formatting function
const formatPrice = (price) => {
  if (price === undefined || price === null || isNaN(price)) return "0₫";
  return Number(price).toLocaleString("vi-VN", {
    style: "currency",
    currency: "VND",
  });
};
// --- END NAVIGATION & UTILS ---

// Xóa sản phẩm khỏi giỏ hàng
const removeCartItem = async (variantId) => {
  const url = `/api/cart-items/remove?variantId=${variantId}`; 
  try {
    await api.delete(url); 
    cartItems.value = cartItems.value.filter(item => item.variantId !== variantId);
    calculateTotal();
  } catch (error) {
    console.error("Lỗi khi xóa sản phẩm:", error);
  }
};

// Cập nhật số lượng sản phẩm trong giỏ hàng
const updateCartItemQuantity = async (variantId, newQuantity) => { 
  if (newQuantity < 1) {
    console.warn("Số lượng không thể nhỏ hơn 1.");
    return; 
  }

  const url = `/api/cart-items/update?variantId=${variantId}&quantity=${newQuantity}`;
  console.log("[DEBUG] Gọi updateQuantity với URL:", url); 

  try {
    await api.put(url);
    console.log("[DEBUG] API update thành công."); 

    await fetchCartItems(); 
    console.log("[DEBUG] Đã fetch lại cart items sau khi update.");

  } catch (error) {
    console.error("Lỗi khi cập nhật số lượng:", error);
    if (error.response) {
        console.error('[DEBUG] Lỗi response từ server khi update:', error.response);
    }
  }
};

//Xóa sạch toàn bộ giỏ hàng
const cleanCart = async () => { 
  if (cartItems.value.length === 0) {
    console.log("Giỏ hàng đã rỗng, không cần xóa.");
    return; 
  }

  const cartCode = cartItems.value[0].cartCode; 

  if (!cartCode) {
    console.error("Lỗi: Không tìm thấy cartCode trong item đầu tiên!");
    return;
  }

  const url = `/api/cart-items/clean?cartCode=${cartCode}`;
  console.log("[DEBUG] Gọi cleanCart với URL:", url); 

  try {
    await api.delete(url);
    cartItems.value = [];
    cartTotal.value = 0;
    console.log("Đã xóa toàn bộ giỏ hàng thành công.");
  } catch (error) {
    console.error("Lỗi khi xóa toàn bộ giỏ hàng:", error);
    if (error.response) {
        console.error('[DEBUG] Lỗi response từ server khi clean:', error.response);
    }
  }
};

const handleCartUpdate = () => {
    fetchCartItems();
};

onMounted(() => {
    emitter.on('cart-updated', handleCartUpdate);
    fetchCategories(); // Giữ lại dòng này
});

onUnmounted(() => {
    emitter.off('cart-updated', handleCartUpdate);
});
</script>