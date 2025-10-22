<template>
  <div class="admin-container">
    <div class="form-section card">
      <h2>{{ isEdit ? 'Edit Product Information' : 'Create New Product' }}</h2>
      <div class='form-grid'>
        <input v-model='product.productCode' placeholder='Product Code' :disabled='isEdit' />
        <input v-model='product.name' placeholder='Product Name' />
        <input v-model='product.description' placeholder='Description' class="form-field-full-width" />
        <input type="number" v-model.number='product.price' placeholder='Price' />
        <input type="number" v-model.number='product.discount' placeholder='Discount' />
        <input type="number" v-model.number='product.stockQuantity' placeholder='Stock Quantity' />
        
        <select v-model="product.category.categoryCode">
          <option disabled value="">-- Please select a Category --</option>
          <option v-for="cat in categories" :key="cat.categoryCode" :value="cat.categoryCode">
            {{ cat.name }}
          </option>
        </select>

      </div>
      <div class='btn-group'>
        <button class="btn btn-primary" @click='submitForm'>
          {{ isEdit ? "Update Product" : "Create Product" }}
        </button>
        <button class="btn btn-secondary" @click='resetForm'>Reset</button>
      </div>
    </div>

    <div class="table-section card">
      <div class="table-header">
        <h3>Product List</h3>
        <input type="text" v-model="searchQuery" class="search-input" placeholder="Search by code, name, category...">
      </div>
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th class="col-no">No</th>
              <th class="col-code">Code</th>
              <th class="col-name">Name</th>
              <th class="col-price">Price</th>
              <th class="col-qty">Quantity</th>
              <th class="col-category">Category</th>
              <th class="col-date">Updated At</th>
              <th class="col-action">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for='(p, i) in filteredProducts' :key='p.productCode'>
              <td>{{ i + 1 }}</td>
              <td>{{ p.productCode }}</td>
              <td>{{ p.name }}</td>
              <td class="text-right">{{ formatPrice(p.price) }}</td>
              <td class="text-right">{{ p.stockQuantity }}</td>
              
              <td>{{ p.category ? p.category.name : 'N/A' }}</td>

              <td>{{ formatDate(p.updatedAt) }}</td>
              <td class="action-buttons">
                <button class="btn-icon" @click='editProduct(p)' title="Edit Product">
                  <svg xmlns="http://www.w3.org/2000/svg" height="20px" viewBox="0 0 24 24" width="20px" fill="#6c757d"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34a.9959.9959 0 00-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>
                </button>
                <button class="btn-icon btn-danger-icon" @click='deleteProduct(p.productCode)' title="Delete Product">
                  <svg xmlns="http://www.w3.org/2000/svg" height="20px" viewBox="0 0 24 24" width="20px" fill="#6c757d"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";

// API URLs
const API_PRODUCTS_URL = "http://localhost:8080/api/manager/products";
const API_CATEGORIES_URL = "http://localhost:8080/api/manager/categories";

// State
const products = ref([]);
const categories = ref([]);
const product = ref({
  productCode: "",
  name: "",
  description: "",
  price: 0,
  discount: 0,
  stockQuantity: 0,
  category: { // SỬA 3: Dùng object category lồng nhau
    categoryCode: "" 
  }
});
const isEdit = ref(false);
const searchQuery = ref('');

// Computed
const filteredProducts = computed(() => {
  if (!searchQuery.value) return products.value;
  const lowerCaseQuery = searchQuery.value.toLowerCase();
  return products.value.filter(p =>
    (p.productCode ?? '').toLowerCase().includes(lowerCaseQuery) ||
    (p.name ?? '').toLowerCase().includes(lowerCaseQuery) ||
    (p.category?.name ?? '').toLowerCase().includes(lowerCaseQuery)
  );
});

// Fetch products
const getProducts = async () => {
  try {
    const res = await fetch(API_PRODUCTS_URL);
    if (!res.ok) throw new Error("Failed to fetch products");
    products.value = await res.json();
  } catch (err) {
    console.error("Error fetching products:", err);
    alert("Error fetching products. Check console.");
  }
};

// Fetch categories
const getCategories = async () => {
  try {
    const res = await fetch(API_CATEGORIES_URL);
    if (!res.ok) throw new Error("Failed to fetch categories");
    categories.value = await res.json();
  } catch (err) {
    console.error("Error fetching categories:", err);
    alert("Error fetching categories. Check console.");
  }
};

// Reset form
const resetForm = () => {
  product.value = {
    productCode: "", name: "", description: "", price: 0, discount: 0, stockQuantity: 0,
    category: { categoryCode: "" }
  };
  isEdit.value = false;
};

// Create / Update
const submitForm = async () => {
  try {
    const method = isEdit.value ? "PUT" : "POST";
    const url = isEdit.value
      ? `${API_PRODUCTS_URL}/${product.value.productCode}`
      : API_PRODUCTS_URL;

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(product.value)
    });

    if (!res.ok) {
        // Cố gắng đọc lỗi từ backend để hiển thị
        const errorData = await res.json().catch(() => ({ message: "Submit failed with status: " + res.status }));
        throw new Error(errorData.message || "Submit failed");
    }
    
    alert("Success!");
    resetForm();
    getProducts();
  } catch (err) {
    console.error(err);
    alert(`Error: ${err.message}`);
  }
};

// SỬA 4: Thêm lại đầy đủ logic cho hàm delete
const deleteProduct = async (productCode) => {
  if (!confirm(`Bạn có chắc chắn muốn xóa sản phẩm "${productCode}" không?`)) {
    return;
  }
  try {
    const res = await fetch(`${API_PRODUCTS_URL}/${productCode}`, { 
      method: "DELETE" 
    });

    if (!res.ok) {
      throw new Error("Delete failed");
    }
    
    alert("Product deleted successfully!");
    getProducts();
  } catch (err) {
    console.error("Error deleting product:", err);
    alert("Error deleting product. Check console.");
  }
};

// SỬA 5: Sửa lại hàm editProduct cho an toàn
const editProduct = (p) => {
  // Dùng deep copy để tránh các vấn đề về tham chiếu
  product.value = JSON.parse(JSON.stringify(p)); 
  // Nếu sản phẩm không có category (dữ liệu cũ), tạo object rỗng để v-model không lỗi
  if (!product.value.category) {
      product.value.category = { categoryCode: "" };
  }
  isEdit.value = true;
  document.querySelector('.form-section')?.scrollIntoView({ behavior: 'smooth' });
};

// Format functions
const formatDate = (datetime) => {
  if (!datetime) return "";
  return new Date(datetime).toLocaleString("vi-VN", {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
  });
};
const formatPrice = (price) => {
    if (price === undefined || price === null) return "";
    return Number(price).toLocaleString("vi-VN", { style: "currency", currency: "VND" });
};

// Load data on mount
onMounted(() => {
  getProducts();
  getCategories();
});
</script>
<style scoped>

/* THAY ĐỔI: Cập nhật CSS đồng bộ với CategoryManager */
/* GENERAL LAYOUT */
.admin-container {
  padding: 24px;
  background-color: #f0f2f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
.card {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}
.form-section {
  margin-bottom: 24px;
}

/* TYPOGRAPHY & HEADER */
h2 {
  color: #333;
  margin: 0 0 24px 0;
  font-weight: 600;
}
.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.table-header h3 {
  margin: 0;
  color: #333;
  font-weight: 600;
}
.search-input {
  width: 300px;
  padding: 10px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s;
}
.search-input:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

/* FORM STYLES */
.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}
input, select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s;
}
input:focus, select:focus {
  outline: none;
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

/* BUTTON STYLES */
.btn-group {
  display: flex;
  gap: 12px;
}
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}
.btn-primary {
  background-color: #1890ff;
  color: white;
}
.btn-primary:hover {
  background-color: #40a9ff;
}
.btn-secondary {
  background-color: #fff;
  color: #333;
  border: 1px solid #d9d9d9;
}
.btn-secondary:hover {
  border-color: #1890ff;
  color: #1890ff;
}

/* TABLE STYLES */
.table-wrapper {
  overflow-x: auto;
}
table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}
th, td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
  vertical-align: middle;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
thead th {
  background-color: #fafafa;
  font-weight: 600;
  color: #000;
  text-transform: uppercase;
  font-size: 12px;
}
tbody tr:hover {
  background-color: #f5f5f5;
}
/* Column Widths for Product Manager */
.col-no { width: 5%; }
.col-code { width: 15%; }
.col-name { width: 25%; }
.col-price { width: 15%; }
.col-qty { width: 10%; }
.col-category { width: 10%; }
.col-date { width: 10%; }
.col-action { width: 10%; text-align: center; }

.text-right {
  text-align: right;
}

/* Icon Button Styles */
.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
}
.btn-icon {
    background: transparent;
    border: none;
    cursor: pointer;
    padding: 6px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background-color 0.2s;
}
.btn-icon:hover {
    background-color: #e9ecef;
}
.btn-icon svg {
    width: 20px;
    height: 20px;
    fill: #6c757d;
    transition: fill 0.2s;
}
.btn-icon:hover svg {
    fill: #1890ff;
}
.btn-icon.btn-danger-icon:hover svg {
    fill: #f5222d;
}
</style>