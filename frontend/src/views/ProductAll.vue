<template>
  <div class="product-all-container">
    <div class="search-filter-bar">
      <input
        v-model="searchKeyword"
        @keyup.enter="fetchProducts"
        type="text"
        placeholder="Tìm kiếm sản phẩm..."
        class="search-input"
      />
      <select v-model="filter" @change="fetchProducts" class="filter-select">
        <option value="all">Tất cả</option>
        <option value="new">Mới nhất</option>
        <option value="old">Cũ nhất</option>
        <option value="price-asc">Giá tăng dần</option>
        <option value="price-desc">Giá giảm dần</option>
        <option value="best-selling">Bán chạy</option>
        <option value="discounted">Giảm giá</option>
      </select>
      <button @click="fetchProducts" class="btn-search">Tìm kiếm</button>
    </div>
    <div class="product-all-grid">
      <div
        class="product-item"
        v-for="product in products"
        :key="product.productCode"
        @click="goToDetail(product.productCode)"
      >
        <img
          :src="product.imageUrl || '/src/assets/images/products/tsh1.jpg'"
          :alt="product.name"
        />
        <h2>{{ product.name }}</h2>
        <div class="price-row">
          <span class="discount" v-if="product.discount > 0">
            -{{ Math.round((product.discount / product.price) * 100) }}%
          </span>
          <span class="price">{{ formatPrice(product.price - product.discount) }}</span>
          <span class="old-price" v-if="product.discount > 0">
            {{ formatPrice(product.price) }}
          </span>
        </div>
      </div>
    </div>
    <div class="pagination">
      <button :disabled="page === 0" @click="prevPage">Trước</button>
      <span>Trang {{ page + 1 }}</span>
      <button :disabled="!hasNext" @click="nextPage">Sau</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
const products = ref([]);
const searchKeyword = ref("");
const filter = ref("all");
const page = ref(0);
const size = ref(10);
const hasNext = ref(false);


const router = useRouter();

const goToDetail = (code) => {
  router.push({ name: "ProductDetail", params: { code } });
};

const fetchProducts = async () => {
  let url = "";
  let params = `?page=${page.value}&size=${size.value}`;

  if (searchKeyword.value) {
    url = `/api/products/search${params}&keyword=${encodeURIComponent(
      searchKeyword.value
    )}`;
  } else {
    switch (filter.value) {
      case "all":
        url = `/api/products/page${params}`;
        break;
      case "new":
        url = `/api/products/new${params}`;
        break;
      case "old":
        url = `/api/products/page${params}&sort=createdAt,asc`;
        break;
      case "price-asc":
        url = `/api/products/page${params}&sort=price,asc`;
        break;
      case "price-desc":
        url = `/api/products/page${params}&sort=price,desc`;
        break;
      case "best-selling":
        url = `/api/products/best-selling${params}`;
        break;
      case "discounted":
        url = `/api/products/discounted${params}`;
        break;
      default:
        url = `/api/products/page${params}`;
    }
  }

  try {
    const res = await fetch(url);
    if (!res.ok) throw new Error("Lỗi khi lấy sản phẩm");
    const data = await res.json();
    products.value = Array.isArray(data) ? data : data.content || [];
    hasNext.value = data.totalPages ? page.value < data.totalPages - 1 : false;
  } catch (e) {
    products.value = [];
    hasNext.value = false;
  }
};

const prevPage = () => {
  if (page.value > 0) {
    page.value--;
    fetchProducts();
  }
};
const nextPage = () => {
  if (hasNext.value) {
    page.value++;
    fetchProducts();
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
});
</script>

<style scoped>
.product-all-container {
  max-width: 1400px;
  margin: 100px auto 40px auto;
  padding: 20px;
}
.search-filter-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  align-items: center;
}
.search-input {
  flex: 1;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
}
.price-input {
  width: 120px;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
}
.filter-select {
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
}
.btn-search {
  padding: 10px 20px;
  background: black;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.product-all-grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(200px, 1fr));
  background: #d9d9d9;
  gap: 20px;
  width: 100%;
  justify-content: center;
  margin: auto;
  border-radius: 5px;
  padding: 30px;
}

.product-item {
  background: #f7f7f7;
  border-radius: 10px;
  padding: 15px;
  text-align: center;
  box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.05);
}
.product-item img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 8px;
}
.product-item h2 {
  font-size: 18px;
  margin: 10px 0 4px 0;
}
.product-item h4 {
  color: #ff6600;
  font-size: 16px;
  margin: 0;
}
.price-row {
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: center; /* thêm dòng này để căn giữa toàn bộ nội dung */
}
.discount {
  color: red;
  font-weight: bold;
  font-size: 1rem;
}
.price {
  color: #000;
  font-weight: bold;
  font-size: 1.2rem;
}
.old-price {
  color: gray;
  text-decoration: line-through;
  font-size: 1rem;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-top: 20px;
}

@media (max-width: 1024px) {
  .product-all-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 600px) {
  .product-all-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
